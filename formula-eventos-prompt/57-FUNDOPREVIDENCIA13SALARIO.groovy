if (TipoMatricula.AUTONOMO.equals(matricula.tipo) || TipoMatricula.ESTAGIARIO.equals(matricula.tipo)) {
    suspender "Este cálculo não é executado para autônomos e estagiários"
}
if (!Funcoes.recebeDecimoTerceiro()) {
    suspender "A matrícula não tem direito a receber décimo terceiro"
}
valorCalculado = 0
if (Funcoes.possuiPrevidencia(TipoPrevidencia.PREVIDENCIA_PROPRIA)) {
    def baseprev = Bases.valor(Bases.FUNDPREV13) - Eventos.valor(158)
    if (baseprev <= 0) {
        suspender 'Não há valor de base de fundo de previdência em décimo terceiro para cálculo'
    }
    int forma = 1 //1-Aplicar %, 2-Usar tabela fundo previdência
    def vaux
    def taxa
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorReferencia = vvar
        valorCalculado = vvar
    } else {
        if (forma == 1) {
            vaux = baseprev
            taxa = evento.taxa
        } else {
            vaux = EncargosSociais.RPPS.PrevidenciaPropria.buscaMaior(1)
            if (baseprev <= vaux) {
                vaux = baseprev
            }
            def vaux2 = Numeros.trunca(vaux, 2)
            taxa = EncargosSociais.RPPS.PrevidenciaPropria.buscaContribuicao(vaux2, 2)
        }
        valorReferencia = taxa
        valorCalculado = (vaux * taxa) / 100
    }
    Bases.compor(valorCalculado, Bases.DEDUCIRRF13, Bases.ABATIRRF13)
}
valorCalculado -= Funcoes.buscaValorEvento13SalarioIntegralAdiantado()
