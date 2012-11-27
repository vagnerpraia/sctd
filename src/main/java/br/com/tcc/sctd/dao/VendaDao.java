/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tcc.sctd.dao;

import br.com.caelum.vraptor.ioc.Component;
import br.com.tcc.sctd.model.Venda;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author lpinto
 */
@Component
public class VendaDao extends DaoGenericoImpl<Venda> {

    public VendaDao(Session sessao) {
        super(sessao);
    }
    
    public Venda buscar(Venda venda){
        Criteria criterio = sessao.createCriteria(Venda.class);
        criterio.add(Restrictions.eq("id", venda.getId()));
        return (Venda) criterio.uniqueResult();
    }

    
    public List<Venda> buscarPorDataDeVenda(Date dataInicial, Date dataFinal) {
        Criteria criterio = sessao.createCriteria(Venda.class);
        criterio.add(Restrictions.between("dataVenda", dataInicial, dataFinal));
        
        criterio.addOrder(Order.asc("dataVenda"));
        
        
        return criterio.list();
    }
    
}
