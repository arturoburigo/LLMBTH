Funcoes.somenteFuncionarios()
int tipo = 1  //1 - Normal, 2 - Por faixa
def valorFerias = Funcoes.replicaFeriasNaFolhaMensal(evento.codigo)
valorCalculado = valorFerias.valor
valorReferencia = valorFerias.referencia
if (valorFerias.valor <= 0) {
    if (folha.folhaPagamento) {
        if (!Funcoes.possuiPrevidencia(TipoPrevidencia.PREVIDENCIA_PROPRIA)) {
            suspender "Este cálculo é realizado apenas para funcionários contribuintes da previdência própria"
        }
        int dependentes = servidor.getDependenteSalarioFamilia(TipoSalarioFamilia.ESTATUTARIO)
        if (dependentes < 1) {
            suspender "Não há dependentes de salário família para o funcionário ou não há configuração vigente de salário família do tipo 'Estatutário' na entidade"
        }
        if (Funcoes.cedidocomonus() > 0 && Bases.valor(Bases.PAGAPROP) == 0) {
            suspender "A matrícula está cedida, com ônus, durante toda a competência"
        }
        if (Funcoes.diasgozo() == 0 && !TipoProcessamento.FERIAS.equals(calculo.tipoProcessamento)) {
            def afastamentos = Eventos.valorReferencia(4) + Eventos.valorReferencia(5) + Eventos.valorReferencia(7) + Eventos.valorReferencia(8)
            if (afastamentos > 0 || Bases.valor(Bases.PAGAPROP) == 0) {
                suspender "Não é possível calcular o salário família estatutário para funcionários sem valor de base 'Paga proporcional' ou com afastamento por 'Auxílio doença previdência', 'Doença do trabalho previdência', 'Acidente de trabalho previdência', 'Licença (SEM vencimentos) - Servidor Público' ou 'Serviço militar' na competência"
            }
        } else {
            int diasferiasant
            if (TipoProcessamento.FERIAS.equals(calculo.tipoProcessamento)) {
                if (!periodoConcessao.diasGozo || periodoConcessao.diasGozo <= 0) {
                    suspender "Este evento deve ser calculado apenas em lançamentos de férias com gozo"
                } else {
                    diasferiasant = Funcoes.diasFeriasAnteriores(periodoConcessao.dataInicioGozo)
                }
            }
            int dias = Funcoes.diastrab() + Funcoes.afaslicmat() + Funcoes.afasdirinteg() - diasferiasant
            if (dias > 0) {
                suspender "O evento será calculado no processamento mensal pois há dias trabalhados na competência"
            }
        }
        valorReferencia = dependentes
        def vvar = Lancamentos.valor(evento)
        if (vvar >= 0) {
            valorCalculado = vvar
        } else {
            if (tipo == 1) {
                valorCalculado = dependentes * EncargosSociais.SalarioFamilia.Estatutario.buscaMaior(2)
            } else {
                valorReferencia = EncargosSociais.SalarioFamilia.Estatutario.buscaContribuicao(funcionario.salario, 2)
                valorCalculado = dependentes * valorReferencia
            }
        }
    }
}