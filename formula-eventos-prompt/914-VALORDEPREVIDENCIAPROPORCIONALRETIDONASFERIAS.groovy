// O código calcula o valor proporcional da Previdência retida nas férias de funcionários com Previdências ativas. Ele verifica se há um valor previamente registrado para o evento e o utiliza, caso positivo. Se não houver, ele calcula o valor total da Previdência nas férias e subtrai os valores já descontados no mês. Se o valor resultante for positivo, ele é definido como o valor calculado e o evento é marcado como replicado.

// Função exclusiva para funcionários
Funcoes.somenteFuncionarios()
// Verifica se há Previdências ativas
if (Funcoes.quantPrevidenciasAtivas() > 0) {
    // Variável para armazenar o valor retido
    double valorRetido
    // Variável para armazenar o valor calculado lançado em variavel
    def vvar = Lancamentos.valor(evento)
    // Verifica se há um valor previamente registrado para o evento
    if (vvar > 0) {
        // Se houver, ele é definido como o valor retido
        valorRetido = vvar
    } else {
        // Se não houver, ele calcula o valor total da Previdência nas férias do evento 88, 89, 90, 91 e 249
        def valorPrevidFeriasIntegral = Funcoes.getValorCodigoEventoFerias(88, true).valor + Funcoes.getValorCodigoEventoFerias(89, true).valor + Funcoes.getValorCodigoEventoFerias(90, true).valor + Funcoes.getValorCodigoEventoFerias(91, true).valor + Funcoes.getValorCodigoEventoFerias(249, true).valor
        // Verifica se o valor total é maior que zero
        if (valorPrevidFeriasIntegral > 0) {
            // Se for, ele subtrai os valores já descontados no mês
            def valorPrevidFeriasDoMes = Eventos.valor(902) + Eventos.valor(905) + Eventos.valor(906) + Eventos.valor(907) + Eventos.valor(908)
            valorRetido = valorPrevidFeriasIntegral - valorPrevidFeriasDoMes
        }
    }
    // Se o valor retido for maior que zero, ele é definido como o valor calculado e o evento é marcado como replicado
    if (valorRetido > 0) {
        valorCalculado = valorRetido
        evento.replicado(true)
    }
}
