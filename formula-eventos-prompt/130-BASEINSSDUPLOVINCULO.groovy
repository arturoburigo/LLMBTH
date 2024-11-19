if (TipoMatricula.ESTAGIARIO.equals(matricula.tipo) || TipoMatricula.APOSENTADO.equals(matricula.tipo) || TipoMatricula.PENSIONISTA.equals(matricula.tipo)) {
    suspender"Este cálculo não é executado para Estágiario e Autônomo e Pensionistas"
}
vaux = Lancamentos.valor(evento)
if (vaux >= 0) {
    valorReferencia = vaux
} else {
    valorReferencia = Funcoes.getValorBaseMultiplosVinculos(Bases.INSS, calculo.tipoProcessamento, calculo.subTipoProcessamento)
}
double valorIntegral
if (SubTipoProcessamento.COMPLEMENTAR.equals(calculo.subTipoProcessamento)) {
    valorIntegral += Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, calculo.tipoProcessamento, calculo.subTipoProcessamento)
    valorIntegral += Eventos.valorCalculado(evento.codigo, TipoValor.CALCULADO, calculo.tipoProcessamento, SubTipoProcessamento.INTEGRAL)
    if (TipoProcessamento.MENSAL.equals(calculo.tipoProcessamento)) {
        valorIntegral += Eventos.valorCalculadoMultiplosVinculos(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.RESCISAO, SubTipoProcessamento.INTEGRAL)
        valorIntegral += Eventos.valorCalculadoMultiplosVinculos(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.RESCISAO, SubTipoProcessamento.COMPLEMENTAR)
    }
    if (TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento)) {
        valorIntegral += Eventos.valorCalculadoMultiplosVinculos(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.INTEGRAL)
        valorIntegral += Eventos.valorCalculadoMultiplosVinculos(evento.codigo, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.COMPLEMENTAR)
    }
}
valorCalculado = valorReferencia + valorIntegral
