package dat.daos;

import java.util.List;
import java.util.Set;

public interface IDAO<T> {

    Set<T> getAll();
    T getById(Long id);
    T create (T t);
    T update (T t);
    void delete (T t);
}
