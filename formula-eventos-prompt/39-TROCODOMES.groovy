//Este evento deve ser configurado para calcular por último (Guia Geral > Calcular por último)
if (TipoProcessamento.MENSAL.equals(calculo.tipoProcessamento)) {
    def vvar = Lancamentos.valor(evento)
    if (vvar > 0) {
        valorCalculado = vvar
        valorReferencia = vvar
    }
}
