package br.com.fiap.agnellofrontend.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {ProdutoEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProdutoDao produtoDao();
}
