// Verifica se a matrícula é de tipo FUNCIONARIO ou AUTONOMO, suspendendo o cálculo caso contrário.
if (!TipoMatricula.FUNCIONARIO.equals(matricula.tipo) && !TipoMatricula.AUTONOMO.equals(matricula.tipo)) {
    suspender "Este cálculo é executado apenas para funcionários ou autônomos"
}

// Verifica se a matrícula possui vínculo com a previdência federal, suspendendo o cálculo caso não possua.
if (!Funcoes.possuiPrevidenciaFederal(matricula.tipo)) {
    suspender "Este cálculo é realizado apenas para matrículas contribuintes da previdência federal"
}

// Regras específicas para matrículas do tipo AUTONOMO.
if (TipoMatricula.AUTONOMO.equals(matricula.tipo)) {
    // Cálculo permitido apenas para competências posteriores a abril de 2003.
    if (calculo.competencia < Datas.data(2003, 4, 1)) {
        suspender "Para autônomos, este cálculo deve ser executado apenas em competências posteriores a 03/2003"
    }
    // Suspende o cálculo para autônomos com categoria MEI e código eSocial igual a "741".
    if (autonomo.codESocial.equals("741")) {
        suspender "Não há desconto de contribuição previdenciária para autônomos da categoria MEI com o 'Código eSocial' igual a '741' informado na categoria do trabalhador"
    }
}

// Suspende o cálculo caso ele esteja sendo realizado em processamentos de décimo terceiro salário.
if (TipoProcessamento.DECIMO_TERCEIRO_SALARIO.equals(calculo.tipoProcessamento)) {
    suspender "Este evento não é calculado em processamentos de décimo terceiro"
}

// Verifica se o cálculo está sendo feito em processamento complementar e se já foi calculado antes.
if (SubTipoProcessamento.COMPLEMENTAR.equals(calculo.subTipoProcessamento)) {
    if (Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, calculo.tipoProcessamento, SubTipoProcessamento.INTEGRAL) > 0 || 
        Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, calculo.tipoProcessamento, calculo.subTipoProcessamento) > 0) {
        suspender "O evento já foi calculado no mesmo processamento para outro subtipo de cálculo nesta competência"
    }
}

// Caso seja uma folha de pagamento:
if (folha.folhaPagamento) {
    // Obtém o valor base do evento.
    def base = Lancamentos.valor(evento)

    // Verifica se o valor base é menor ou igual a zero.
    if (base <= 0) {
        // Regras para processamento de férias.
        if (TipoProcessamento.FERIAS.equals(calculo.tipoProcessamento)) {
            // Suspende o cálculo se não houver dias de gozo informados.
            if (periodoConcessao.diasGozo <= 0) {
                suspender "Este evento não é calculado no processamento de férias quando não há dias de gozo informados"
            } else {
                // Verifica se o evento já foi calculado em um processamento de férias anterior.
                def cptInicioGozo = Datas.data(Datas.ano(periodoConcessao.dataInicioGozo), Datas.mes(periodoConcessao.dataInicioGozo), 1)
                if (Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.FERIAS, SubTipoProcessamento.INTEGRAL, cptInicioGozo) > 0) {
                    suspender "O evento já foi calculado em processamento de férias anterior com gozo"
                }
                // Busca valores de INSS em outras empresas para autônomos.
                base = BasesOutrasEmpresas.buscaPor(TipoProcessamento.MENSAL, cptInicioGozo, OrigemBaseOutraEmpresa.AUTONOMO).sum(0, { it.baseInss })
            }
        } else {
            // Regras para processamento de rescisão.
            if (TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento)) {
                if (Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.FERIAS, SubTipoProcessamento.INTEGRAL) > 0 || 
                    Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.INTEGRAL) > 0 || 
                    Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.COMPLEMENTAR) > 0) {
                    suspender "O evento já foi calculado em processamento mensal ou de férias nesta competência"
                }
            }
            // Regras para processamento mensal.
            if (TipoProcessamento.MENSAL.equals(calculo.tipoProcessamento)) {
                double valorFerias = 0
                // Verifica se o evento já foi calculado em um processamento de férias na mesma competência.
                folhasPeriodo.buscaFolhasProcessamento(TipoProcessamento.FERIAS).find { f -> 
                    f.folhaPagamento && f.inicioGozoFeriasCalculadas &&
                    Datas.mes(Funcoes.paraData(f.dataPagamento)).equals(Datas.mes(calculo.competencia)) &&
                    Datas.mes(Funcoes.paraData(f.inicioGozoFeriasCalculadas)).equals(Datas.mes(calculo.competencia)) &&
                    f.eventos.each { e -> if (e.codigo == evento.codigo) { valorFerias += e.valor } }
                }
                if (valorFerias > 0) {
                    suspender "O evento já foi calculado no processamento de férias com gozo nesta competência"
                }
            }
            // Busca valores de INSS em outras empresas para autônomos.
            base = BasesOutrasEmpresas.buscaPor(TipoProcessamento.MENSAL, calculo.competencia, OrigemBaseOutraEmpresa.AUTONOMO).sum(0, { it.baseInss })
        }
    }

    // Suspende o cálculo se não houver valores de INSS de autônomos de outras empresas.
    if (base <= 0) {
        suspender "Não há valor lançado de base de INSS de outras empresas originado de autônomos para o servidor"
    }

    // Calcula o valor proporcional com base na unidade de remuneração.
    def remuneracao = Funcoes.remuneracao(matricula.tipo).unidade
    if (calculo.quantidadeDiasCompetencia.equals(31) && (remuneracao.equals(UnidadePagamento.HORISTA) || remuneracao.equals(UnidadePagamento.DIARISTA))) {
        valorCalculado = Funcoes.calcprop(base, Funcoes.cnvdpbase(31))
    } else {
        valorCalculado = Funcoes.calcprop(base, Funcoes.cnvdpbase(30))
    }

    // Composição de base de INSS para autônomos.
    Bases.compor(valorCalculado, Bases.INSSOUTAUTO)
}
