/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tcc.sctd.dao;

import br.com.caelum.vraptor.ioc.Component;
import br.com.tcc.sctd.model.Especialidade;
import org.hibernate.Session;

/**
 *
 * @author LeandroVBOX
 */
@Component
public class EspecialidadeDao extends DaoGenericoImpl<Especialidade> {

    public EspecialidadeDao(Session sessao) {
        super(sessao);
    }
    
}
