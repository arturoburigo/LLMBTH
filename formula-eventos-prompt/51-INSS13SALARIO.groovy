Funcoes.somenteFuncionarios()
if (!Funcoes.recebeDecimoTerceiro()) {
    suspender "A matrícula não tem direito a receber décimo terceiro"
}
if (SubTipoProcessamento.ADIANTAMENTO.equals(calculo.subTipoProcessamento)) {
    suspender "O evento não é calculado no subtipo de processamento 'adiantamento'"
}
valorCalculado = 0
if (funcionario.possuiPrevidenciaFederal) {
    def vaux = Lancamentos.valor(evento)
    if (vaux >= 0) {
        valorReferencia = vaux
        valorCalculado = vaux
    } else {
        double baseOutrosVinculos
        double inssOutrosVinculos
        if (funcionario.possuiMultiploVinculo) {
            baseOutrosVinculos = Funcoes.getValorBaseMultiplosVinculos(Bases.INSS13, calculo.tipoProcessamento, calculo.subTipoProcessamento)
            inssOutrosVinculos = Eventos.valorCalculadoMultiplosVinculos(evento.codigo, TipoValor.CALCULADO, calculo.tipoProcessamento, calculo.subTipoProcessamento)
            if (SubTipoProcessamento.COMPLEMENTAR.equals(calculo.subTipoProcessamento)) {
                inssOutrosVinculos += Eventos.valorCalculadoMultiplosVinculos(evento.codigo, TipoValor.CALCULADO, calculo.tipoProcessamento, SubTipoProcessamento.INTEGRAL)
            }
            if (TipoProcessamento.DECIMO_TERCEIRO_SALARIO.equals(calculo.tipoProcessamento)) {
                inssOutrosVinculos += Eventos.valorCalculadoMultiplosVinculos(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.RESCISAO, SubTipoProcessamento.INTEGRAL)
                inssOutrosVinculos += Eventos.valorCalculadoMultiplosVinculos(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.RESCISAO, SubTipoProcessamento.COMPLEMENTAR)
            }
            if (TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento)) {
                inssOutrosVinculos += Eventos.valorCalculadoMultiplosVinculos(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.DECIMO_TERCEIRO_SALARIO, SubTipoProcessamento.INTEGRAL)
            }
        }
        double baseInss13 = Bases.valor(Bases.INSS13) + Bases.valor(Bases.INSSOUTRA13) + baseOutrosVinculos
        if (TipoProcessamento.MENSAL.equals(calculo.tipoProcessamento)) {
            baseInss13 += Bases.valorCalculado(Bases.INSS13, TipoProcessamento.DECIMO_TERCEIRO_SALARIO, SubTipoProcessamento.INTEGRAL) + Bases.valorCalculado(Bases.INSSOUTRA13, TipoProcessamento.DECIMO_TERCEIRO_SALARIO, SubTipoProcessamento.INTEGRAL)
        }
        if (baseInss13 <= 0) {
            suspender "Não há valor de base de INSS de décimo terceiro ou INSS de outras empresas para cálculo"
        }
        def tetoInss = EncargosSociais.RGPS.buscaMaior(1)
        if (baseInss13 > tetoInss) {
            double excedenteInss = baseInss13 - tetoInss
            Bases.compor(excedenteInss, Bases.EXCEINSS13)
            baseInss13 = tetoInss
        }
        double calculaContribuicao = Numeros.trunca(baseInss13, 2)
        if (funcionario.conselheiroTutelar) {
            valorReferencia = 11
        } else {
            valorReferencia = EncargosSociais.RGPS.buscaContribuicao(calculaContribuicao, 2)
        }
        if (Funcoes.inicioCompetencia() >= Datas.data(2020, 3, 1) && !funcionario.conselheiroTutelar) {
            vaux = Funcoes.calculoProgressivoINSS(baseInss13)
            valorCalculado = Numeros.arredonda(vaux, 2) - inssOutrosVinculos - Eventos.valor(171)
        } else {
            vaux = (baseInss13 * valorReferencia) / 100
            valorCalculado = Numeros.trunca(vaux, 2) - inssOutrosVinculos - Eventos.valor(171)
        }
        if (TipoProcessamento.MENSAL.equals(calculo.tipoProcessamento)) {
            valorCalculado -= Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.DECIMO_TERCEIRO_SALARIO, SubTipoProcessamento.INTEGRAL)
        }
    }
    Bases.compor(valorCalculado, Bases.DEDUCIRRF13, Bases.ABATIRRF13)
}
valorCalculado -= Funcoes.buscaValorEvento13SalarioIntegralAdiantado()
