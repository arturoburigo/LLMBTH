// O código verifica se o funcionário possui Previdência Federal. Se o evento já foi lançado em variável (vvar), ele usa esse valor como abatimento. Caso contrário, ele verifica o valor integral do evento de férias (código 88). Se esse valor for positivo, o valor do evento 902 é utilizado como abatimento. Se o abatimento calculado for maior que zero, ele é atribuído a valorCalculado e o evento é marcado como replicado.

Funcoes.somenteFuncionarios()
if (funcionario.possuiPrevidenciaFederal) {
    double valorAbatimento
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorAbatimento = vvar
    } else {
        def valorInssFeriasIntegral = Funcoes.getValorCodigoEventoFerias(88, true).valor
        if (valorInssFeriasIntegral > 0) {
            valorAbatimento = Eventos.valor(902)
        }
    }
    if (valorAbatimento > 0) {
        valorCalculado = valorAbatimento
        evento.replicado(true)
    }
}
