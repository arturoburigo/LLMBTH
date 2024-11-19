//O código verifica se o funcionário possui a Previdência Estadual. Se o evento já foi lançado em variável (vvar), ele utiliza esse valor como abatimento. Caso contrário, o código verifica se há um valor integral associado ao evento de férias (código 89). Se esse valor for positivo, ele usa o valor do evento 905 como abatimento. Se o valor do abatimento for maior que zero, ele é atribuído a valorCalculado e o evento é marcado como replicado.

Funcoes.somenteFuncionarios()
if (funcionario.possuiPrevidencia(TipoPrevidencia.PREVIDENCIA_ESTADUAL)) {
    double valorAbatimento
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorAbatimento = vvar
    } else {
        def valorIpescFeriasIntegral = Funcoes.getValorCodigoEventoFerias(89, true).valor
        if (valorIpescFeriasIntegral > 0) {
            valorAbatimento = Eventos.valor(905)
        }
    }
    if (valorAbatimento > 0) {
        valorCalculado = valorAbatimento
        evento.replicado(true)
    }
}
