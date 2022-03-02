package me.saywut.uuidfix;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.sql.SQLException;
import java.util.logging.Level;

public final class UUIDFix extends Plugin {

    public MySQL SQL;
    private Configuration config;
    private static UUIDFix instance;

    public static UUIDFix getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadConfig();
        instance = this;
        this.SQL = new MySQL();

        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            //e.printStackTrace();
            getLogger().info("Not connected to Authme Database");
        }

        if (SQL.isConnected()) {
            getLogger().info("Connected to AuthMe Database");
        }
        getProxy().getPluginManager().registerListener(this, new PlayerJoin(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SQL.disconnect();
        getLogger().info("Disconnected from Authme Database");
    }

    public void loadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource("config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File loadResource(String resource) {
        File folder = this.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = this.getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            this.getLogger().log(Level.SEVERE, "Exception while writing default config", e);
        }
        return resourceFile;
    }

    public Configuration getConfig() {
        return this.config;
    }
}
