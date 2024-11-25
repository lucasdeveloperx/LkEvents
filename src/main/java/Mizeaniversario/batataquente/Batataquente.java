package Mizeaniversario.batataquente;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Batataquente extends JavaPlugin implements Listener {

    private List<Player> jogadoresAtivos = new ArrayList<>();
    private Player jogadorComBatata;
    private BossBar bossBar;
    private boolean jogoAtivo = false;
    private int tempoRestante = 20; // Tempo de 20 segundos por rodada
    private Location lobbyLocation;
    private Location arenaLocation;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Batata Quente Plugin Ativado!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Batata Quente Plugin Desativado!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Apenas jogadores podem usar este comando!");
            return true;
        }

        Player jogador = (Player) sender;

        if (label.equalsIgnoreCase("batata")) {
            if (args.length == 0) {
                jogador.sendMessage(ChatColor.RED + "Uso correto: /batata entrar, /batata sair, /batata começar, /batata setlobby, /batata setarena.");
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "entrar":
                    if (jogoAtivo) {
                        jogador.sendMessage(ChatColor.RED + "O jogo já está em andamento!");
                        return true;
                    }
                    if (!jogadoresAtivos.contains(jogador)) {
                        jogadoresAtivos.add(jogador);
                        jogador.teleport(lobbyLocation);
                        jogador.sendMessage(ChatColor.GREEN + "Você entrou na fila de Batata Quente!");
                        Bukkit.broadcastMessage(ChatColor.GREEN + jogador.getName() + " entrou na fila! Jogadores na fila: " + jogadoresAtivos.size());

                        // Adiciona título e subtítulo
                        jogador.sendTitle(ChatColor.YELLOW + "Batata Quente", ChatColor.GREEN + "Você entrou!", 10, 70, 20);
                    } else {
                        jogador.sendMessage(ChatColor.RED + "Você já está na fila.");
                    }
                    break;

                case "sair":
                    if (jogadoresAtivos.contains(jogador)) {
                        jogadoresAtivos.remove(jogador);
                        jogador.sendMessage(ChatColor.GREEN + "Você saiu da fila de Batata Quente.");
                        Bukkit.broadcastMessage(ChatColor.RED + jogador.getName() + " saiu da fila.");
                    } else {
                        jogador.sendMessage(ChatColor.RED + "Você não está na fila.");
                    }
                    break;

                case "começar":
                    if (jogadoresAtivos.size() < 2) {
                        jogador.sendMessage(ChatColor.RED + "É necessário pelo menos 2 jogadores para começar.");
                        return true;
                    }
                    if (!jogoAtivo) {
                        iniciarJogo();
                    } else {
                        jogador.sendMessage(ChatColor.RED + "O jogo já foi iniciado.");
                    }
                    break;

                case "setlobby":
                    lobbyLocation = jogador.getLocation();
                    jogador.sendMessage(ChatColor.GREEN + "Lobby de Batata Quente definido!");
                    break;

                case "setarena":
                    arenaLocation = jogador.getLocation();
                    jogador.sendMessage(ChatColor.GREEN + "Arena de Batata Quente definida!");
                    break;
            }
        }
        return true;
    }

    private void iniciarJogo() {
        if (lobbyLocation == null || arenaLocation == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Você precisa definir o lobby e a arena antes de começar!");
            return;
        }

        jogoAtivo = true;
        Bukkit.broadcastMessage(ChatColor.YELLOW + "O jogo de Batata Quente começou!");
        teleportarJogadoresParaArena();
        iniciarNovaRodada();
    }

    private void teleportarJogadoresParaArena() {
        for (Player jogador : jogadoresAtivos) {
            jogador.teleport(arenaLocation);
            jogador.setGameMode(GameMode.ADVENTURE);
            jogador.getInventory().clear();
        }
    }

    private void iniciarNovaRodada() {
        if (jogadoresAtivos.size() == 1) {
            encerrarJogo(jogadoresAtivos.get(0));
            return;
        }

        tempoRestante = 20; // Tempo para a nova rodada
        Random random = new Random();
        jogadorComBatata = jogadoresAtivos.get(random.nextInt(jogadoresAtivos.size()));

        // Dar a batata ao jogador
        ItemStack batata = new ItemStack(Material.POTATO);
        jogadorComBatata.getInventory().addItem(batata);
        jogadorComBatata.setGlowing(true);
        jogadorComBatata.sendTitle(ChatColor.RED + "Você está com a batata!", ChatColor.YELLOW + "Passe para outro jogador!", 10, 40, 10);

        // Boss bar para indicar o tempo restante
        if (bossBar != null) bossBar.removeAll();
        bossBar = Bukkit.createBossBar(ChatColor.RED + "Tempo restante", BarColor.RED, BarStyle.SOLID);
        bossBar.setProgress(1.0);
        bossBar.setVisible(true);
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (tempoRestante <= 0) {
                    explodirJogador(jogadorComBatata);
                    iniciarNovaRodada();
                    cancel();
                }

                bossBar.setProgress(tempoRestante / 20.0);
                tempoRestante--;
            }
        }.runTaskTimer(this, 0, 20);
    }

    private void explodirJogador(Player jogador) {
        // Mensagem global no título
        Bukkit.getOnlinePlayers().forEach(p -> p.sendTitle(ChatColor.RED + jogador.getName() + " explodiu!", ChatColor.YELLOW + "Batata Quente!", 10, 40, 10));

        // Colocar jogador em modo espectador
        jogador.setGameMode(GameMode.SPECTATOR);
        jogador.getInventory().clear();
        jogador.setGlowing(false);

        // Remover da lista de jogadores ativos
        jogadoresAtivos.remove(jogador);

        // Mensagem no chat
        Bukkit.broadcastMessage(ChatColor.RED + jogador.getName() + " explodiu com a batata!");
    }

    private void encerrarJogo(Player vencedor) {
        jogoAtivo = false;
        bossBar.removeAll();

        // Mensagem global no chat
        Bukkit.broadcastMessage(ChatColor.GOLD + vencedor.getName() + " venceu o jogo de Batata Quente!");

        // Mensagem de título apenas para o vencedor
        vencedor.sendTitle(ChatColor.GREEN + "Você venceu!", ChatColor.YELLOW + "Parabéns!", 10, 70, 20);

        // Mensagem global de título para todos
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendTitle(ChatColor.GREEN + vencedor.getName(), ChatColor.YELLOW + "Venceu a partida!", 10, 40, 10)
        );

        // Teleportar todos os jogadores de volta para o lobby
        for (Player jogador : jogadoresAtivos) {
            jogador.teleport(lobbyLocation);
            jogador.setGameMode(GameMode.SURVIVAL);
        }

        // Garantir que todos os jogadores que estavam no modo espectador também voltem para o lobby e saiam do espectador
        for (Player jogador : Bukkit.getOnlinePlayers()) {
            if (jogador.getGameMode() == GameMode.SPECTATOR) {
                jogador.teleport(lobbyLocation);
                jogador.setGameMode(GameMode.SURVIVAL);
            }
        }

        jogadoresAtivos.clear();
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.POTATO) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Você não pode dropar a batata!");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player jogador = (Player) event.getEntity();
        Player atacante = (Player) event.getDamager();

        // Verifica se o atacante é o jogador com a batata e se o jogador foi tocado
        if (jogadorComBatata == atacante && jogadoresAtivos.contains(jogador)) {
            // Passa a batata para o jogador tocado
            ItemStack batata = new ItemStack(Material.POTATO);
            atacante.getInventory().remove(batata);
            jogador.getInventory().addItem(batata);
            jogador.setGlowing(true);
            jogador.sendTitle(ChatColor.RED + "Você agora tem a batata!", "", 10, 40, 10);
            atacante.setGlowing(false);
            atacante.sendTitle(ChatColor.YELLOW + "Você perdeu a batata!", "", 10, 40, 10);

            // Atualiza quem está com a batata
            jogadorComBatata = jogador;

            // Envia título global para todos os jogadores
            String mensagem = jogador.getName() + " está com a batata!";
            Bukkit.getOnlinePlayers().forEach(player ->
                    player.sendTitle(ChatColor.YELLOW + mensagem, "", 10, 40, 10)
            );
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (lobbyLocation != null) {
            event.getPlayer().teleport(lobbyLocation);
        }
    }
}
