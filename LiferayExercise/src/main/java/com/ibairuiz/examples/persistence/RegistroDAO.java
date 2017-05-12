package com.ibairuiz.examples.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ibairuiz.examples.persistence.beans.Registro;

@Repository
@Transactional
public class RegistroDAO {
    @PersistenceContext
    private EntityManager entityManager;
    
    public long crearRegistro(Registro registro) {
        entityManager.persist(registro);
        entityManager.flush();
        return registro.getIdRegistro();
    }

    public void actualizarRegistro(Registro registro) {
        entityManager.merge(registro);
    }

    public void borrarRegistro(long idRegistro) {
    	Registro registro = getRegistroPorId(idRegistro);
        if (registro != null) {
            entityManager.remove(registro);
        }
    }

    @Transactional(readOnly = true)
    public Registro getRegistroPorId(long idRegistro) {
        return entityManager.find(Registro.class, idRegistro);
    }
}
