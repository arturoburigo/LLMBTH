//Este código calcula o valor relacionado a um evento para funcionários que possuem Previdência Federal. Se o valor associado ao evento for maior que zero, ele é utilizado como o valor calculado. Caso contrário, o código usa os valores de INSS sobre férias. Após calcular, se o valor for positivo, o evento é marcado como replicado.

//Verifica se é funcionario, caso contratrio para a função
Funcoes.somenteFuncionarios()
//Verifica se o funcionário possui Previdência Federal
if (funcionario.possuiPrevidenciaFederal) {
    // Faz o lançamento do evento em variavel
    def vvar = Lancamentos.valor(evento)
    // Verifica se o valor do evento é maior que zero
    if (vvar > 0) {
        valorCalculado = vvar
    } else {
        // Caso o valor do evento seja menor ou igual a zero, calcula o INSS sobre férias
        def inssFerias = Funcoes.getInssFerias()
        // Atribui o valor de referência e o valor calculado
        valorReferencia = inssFerias.referencia
        valorCalculado = inssFerias.valor
    }
    // Verifica se o valor calculado é maior que zero
    if (valorCalculado > 0) {
        // Marca o evento como replicado
        evento.replicado(true)
    }
}
