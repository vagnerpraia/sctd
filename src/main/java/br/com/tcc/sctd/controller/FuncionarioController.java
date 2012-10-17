/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tcc.sctd.controller;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.tcc.sctd.dao.CargoDao;
import br.com.tcc.sctd.dao.DepartamentoDao;
import br.com.tcc.sctd.dao.FuncionarioDao;
import br.com.tcc.sctd.dao.FuncionarioStatusDao;
import br.com.tcc.sctd.exceptions.DaoException;
import br.com.tcc.sctd.model.Funcionario;
import br.com.tcc.sctd.service.Opcoes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

/**
 *
 * @author LeandroVBOX
 */
@Path("/funcionarios")
@Resource
public class FuncionarioController {

    private static final Logger LOG = Logger.getLogger(FuncionarioController.class);
    private final Result result;
    private final FuncionarioDao funcionarios;
    private List<Opcoes> opcoes;
    private static final int REG_POR_PAGINA = 20;
    private final DepartamentoDao departamentos;
    private final CargoDao cargos;
    private final FuncionarioStatusDao funcionariosStatus;

    /**
     *
     * @param result
     * @param funcionarios
     */
    public FuncionarioController(Result result, FuncionarioDao funcionarios, DepartamentoDao departamentos, 
            CargoDao cargos, FuncionarioStatusDao funcionariosStatus) {
        this.result = result;
        this.funcionarios = funcionarios;
        opcoes = new ArrayList<Opcoes>();
        opcoes.add(new Opcoes("/funcionarios/novo", "Incluir funcionário"));
        this.departamentos = departamentos;
        this.cargos = cargos;
        this.funcionariosStatus = funcionariosStatus;

    }

    @Path(value = {"/", "/index", "/{funcionario}"})
    public void index(Funcionario funcionario) throws DaoException {

        if (funcionario.getNome() != null || funcionario.getCargo() != null || funcionario.getDepartamento() != null) {
            List<Funcionario> listaFuncionarios = funcionarios.buscarPorExemplo(funcionario);
            result.include("funcionarios", listaFuncionarios);
        } else {
            List<Funcionario> listaFuncionarios = funcionarios.buscaPaginada(0, REG_POR_PAGINA, Order.desc("matricula"));
            Long qtdDestaques = funcionarios.getQuantidadeDeFuncionarios();
            Long qtdPaginas = qtdDestaques / REG_POR_PAGINA;
            qtdPaginas += (qtdDestaques % REG_POR_PAGINA > 0) ? 1 : 0;
            result.include("funcionarios", listaFuncionarios);
            result.include("qtde", qtdDestaques);
            result.include("qtdPaginas", qtdPaginas);
            

        }


        result.include("opcoes", opcoes);
        result.include("paginaAtual", 1);
        result.include("cargos", cargos.buscarTodos());
        result.include("departamentos", departamentos.buscarTodos());
        result.include("listastatus", funcionariosStatus.buscarTodos());
    }

    @Path({"/form", "/novo"})
    public void form() throws DaoException {
        result.include("cargos", cargos.buscarTodos());
        result.include("departamentos", departamentos.buscarTodos());
    }

    public void salvar(Funcionario funcionario) throws DaoException {
        funcionario.setDataContratacao(new Date(System.currentTimeMillis()));
        funcionarios.salvar(funcionario);
        result.redirectTo(this).index(null);
    }

    @Path("/editar/{funcionario.matricula}")
    public void editar(Funcionario funcionario) throws DaoException {
        Funcionario func = funcionarios.buscarPorId(funcionario.getMatricula());
        if (func == null) {
            LOG.info("funcionario não encontrado");
            return;
        }


        result.redirectTo(this).formEdicao(func);
    }

    public void formEdicao(Funcionario f) throws DaoException {
        result.include("funcionario", f);
        result.include("opcoes", opcoes);
        result.include("cargos", cargos.buscarTodos());
        result.include("departamentos", departamentos.buscarTodos());
    }

    public void atualizar(Funcionario funcionario) throws DaoException {
        try {
            funcionarios.atualizar(funcionario);
            result.redirectTo(this).index(null);
        } catch (DaoException ex) {
            LOG.error("Erro atualizando o funcionário. " + ex.getMessage());
            result.include("errors", "Erro atualizando o funcionário. " + ex.getMessage());
            result.redirectTo(this).formEdicao(funcionario);
        }
    }
}
