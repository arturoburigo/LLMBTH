//O código verifica se os eventos de cálculo de férias devem ser replicados para cálculos mensais. Se a condição não for atendida (ou seja, não for um processamento mensal ou rescisório, ou sendo rescisório sem necessidade de recalcular mensal), o cálculo é suspenso com a mensagem: "O evento deve ser calculado apenas em processamentos mensais ou rescisórios".


if (calculo.replicaEventosCalculoFeriasParaCalculoMensal) {
    if (!TipoProcessamento.MENSAL.equals(calculo.tipoProcessamento) &&
            (!TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento) || TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento) && !calculo.recalcularMensal)) {
        suspender "O evento deve ser calculado apenas em processamentos mensais ou rescisórios"
    }
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorCalculado = vvar
    }
}
