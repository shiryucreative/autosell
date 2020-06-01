package io.github.shiryu.autosell.api.player;

import com.google.gson.Gson;
import io.github.portlek.database.MapEntry;
import io.github.portlek.database.SQL;
import io.github.shiryu.autosell.AutoSell;
import io.github.shiryu.autosell.api.item.AutoSellItem;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class User {

    private final UUID uuid;

    private final List<AutoSellItem> items = new ArrayList<>();

    public User(@NotNull final UUID uuid){
        this.uuid = uuid;
    }

    public @NotNull UUID getUUID() {
        return this.uuid;
    }

    public @NotNull List<AutoSellItem> getItems() {
        return this.items;
    }

    public void save() {
        final Gson gson = new Gson();
        final SQL sql = AutoSell.getInstance().getSql();


        sql.createTable(
                "autosell",
                Arrays.asList(
                        new MapEntry<>(
                                "uuid",
                                "VARCHAR(128) NOT NULL"
                        ),
                        new MapEntry<>(
                                "items",
                                "LONGTEXT NOT NULL"
                        )
                )
        );

        if (!sql.exists("uuid", this.uuid.toString(), "autosell")){
            sql.insertData(
                    "autosell",
                    Arrays.asList(
                            new MapEntry<>(
                                    "uuid",
                                    this.uuid.toString()
                            ),
                            new MapEntry<>(
                                    "items",
                                    gson.toJson(this.items)
                            )
                    )
            );
        }

        sql.set("items", gson.toJson(this.items), "uuid", "=", this.uuid.toString(), "autosell");
    }
}