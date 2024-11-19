//O código verifica se o tipo de processamento é férias ou décimo terceiro salário. Se for, ele suspende o cálculo com uma mensagem informando que o evento não é calculado nesses tipos de processamento. Em seguida, busca o valor de reembolso de despesas com plano de saúde. Se o valor for zero, suspende o cálculo informando que não há reembolsos disponíveis para os parâmetros da matrícula. Caso o processamento seja rescisório, verifica se o valor de reembolso já foi lançado integralmente em folhas mensais anteriores. Se já foi, suspende o cálculo informando que o valor já foi lançado anteriormente. Caso contrário, ajusta o valor de reembolso para descontar o que já foi lançado. Por fim, o valor do reembolso restante é atribuído como o valor calculado.

// Se o tipo de processamento for férias ou décimo terceiro salário, suspende o cálculo
if (TipoProcessamento.FERIAS.equals(calculo.tipoProcessamento) || TipoProcessamento.DECIMO_TERCEIRO_SALARIO.equals(calculo.tipoProcessamento)) {
    suspender 'Este evento não é calculado em processamentos de férias e décimo terceiro'
}
// Busca o valor de reembolso de despesas com plano de saúde do tipo de segurado, tipo de reembolso e tipo de valor de reembolso informados
double valorReembolso = ReembolsoDespesaPlanoSaude.buscaValor(TipoSegurado.AMBOS, TipoReembolso.AMBOS, TipoValorReembolso.AMBOS)
// Se o valor de reembolso for zero, suspende o cálculo
if (valorReembolso == 0) {
    suspender 'Não há valor de reembolso de depesas com plano de saúde a serem buscados para a matrícula com os parâmetros informados'
}
// Se o tipo de processamento for rescisório, verifica se o valor de reembolso já foi lançado integralmente em folhas mensais anteriores
if (TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento)) {
    // Busca o valor de reembolso de despesas com plano de saúde já reembolsado em folhas mensais anteriores
    double valorReembolsadoMensal = Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.INTEGRAL) + Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.COMPLEMENTAR)
    // Se o valor de reembolso já foi lançado integralmente em folhas mensais anteriores, suspende o cálculo
    if (valorReembolso <= valorReembolsadoMensal) {
        suspender 'O valor de reembolso de depesas com plano de saúde a ser calculado pelo evento já foi lançado em folha mensal anterior'
    } else {
        // Ajusta o valor de reembolso para descontar o que já foi lançado
        valorReembolso -= valorReembolsadoMensal
    }
}
// Atribui o valor de reembolso restante como o valor calculado
valorCalculado = valorReembolso
