package com.ibairuiz.examples.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ibairuiz.examples.persistence.beans.Registro;

/**
 * DAO para bean de Registro. Define los métodos para gestionar la
 * persistencia de las instancias de {@link Registro}.
 * @author ibai.ruiz
 *
 */
@Repository
@Transactional
public class RegistroDAO {
	
	/** Instancia del entityManager. **/
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Método que inserta el registro.
     * @param registro instancia de {@link Registro} a insertar.
     * @return el id de registro insertado.
     */
    public long crearRegistro(Registro registro) {
        entityManager.persist(registro);
        entityManager.flush();
        return registro.getIdRegistro();
    }

    public void actualizarRegistro(Registro registro) {
        entityManager.merge(registro);
    }

    /**
     * Método que borra un registro de la base de datos.
     * @param idRegistro el id de registro a eliminar.
     */
    public void borrarRegistro(long idRegistro) {
    	Registro registro = getRegistroPorId(idRegistro);
        if (registro != null) {
            entityManager.remove(registro);
        }
    }

    /**
     * Método que busca un registro por id.
     * @param idRegistro id del registro a recoger.
     * @return instancia de {@link Registro} devuelta
     */
    @Transactional(readOnly = true)
    public Registro getRegistroPorId(long idRegistro) {
        return entityManager.find(Registro.class, idRegistro);
    }
}
