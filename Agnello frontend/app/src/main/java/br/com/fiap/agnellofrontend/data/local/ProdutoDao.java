package br.com.fiap.agnellofrontend.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

@Dao
public interface ProdutoDao {
    @Query("SELECT * FROM produtos ORDER BY nome")
    Flow<List<ProdutoEntity>> observeAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(ProdutoEntity produto);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsertAll(List<ProdutoEntity> produtos);

    @Delete
    void delete(ProdutoEntity produto);

    @Query("DELETE FROM produtos")
    void clear();
}
