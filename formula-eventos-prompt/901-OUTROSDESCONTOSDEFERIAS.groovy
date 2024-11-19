//O código verifica se os eventos de cálculo de férias devem ser replicados para cálculos mensais. Se a condição não for atendida (não sendo tipo mensal ou rescisório, ou não sendo rescisão sem recalcular mensal), o cálculo é suspenso com a mensagem informando que o evento deve ser calculado apenas em processamentos mensais ou rescisórios. Em seguida, verifica se o evento já foi lançado em variável (vvar). Se o evento tiver um valor maior que zero, esse valor é utilizado como o valor calculado.
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
