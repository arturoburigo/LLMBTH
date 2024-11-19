if (!TipoMatricula.PENSIONISTA.equals(matricula.tipo)) {
    if (Bases.valor(Bases.PAGAPROP) == 0 && Bases.valor(Bases.SALBASE) == 0) {
        suspender "Este evento deve ser calculado para matrículas com valor de base 'Paga proporcional' ou 'Salário base' na competência"
    }
}
if (matricula.possuiMultiploVinculo) {
    suspender 'Para matrículas com múltiplos vínculos será considerada a opção de deduções legais'
}
if (TipoMatricula.AUTONOMO.equals(matricula.tipo) && autonomo.codESocial.equals("741")) {
    suspender "Não há deduções de IRRF para autônomos da categoria MEI com o 'Código eSocial' igual a '741' informado na categoria do trabalhador"
}
if (TipoProcessamento.DECIMO_TERCEIRO_SALARIO.equals(calculo.tipoProcessamento)) {
    suspender "Este evento não é calculado em processamentos de décimo terceiro"
}
if (calculo.rra || TipoProcessamento.PAGAMENTO_ANTERIOR.equals(calculo.tipoProcessamento)) {
    suspender 'Este evento não é calculado em processamentos vinculados a pagamentos anteriores'
}
if (folha.folhaPagamento) {
    def vvar = Lancamentos.valor(evento)
    if (vvar >= 0) {
        valorCalculado = vvar
    } else {
        if (!EncargosSociais.IRRF.deducaoSimplificadaIrrf) {
            suspender "Para apuração da dedução a ser aplicada para a matrícula, a 'Dedução simplificada do IRRF' deve ser informada na manutenção de estabelecimento vigente"
        }
        if (TipoProcessamento.FERIAS.equals(calculo.tipoProcessamento)) {
            if (Eventos.valorCalculado(ClassificacaoEvento.DEDUCSIMPLIRRFMENSAL, TipoValor.CALCULADO, TipoProcessamento.FERIAS, SubTipoProcessamento.INTEGRAL, calculo.competencia) > 0) {
                suspender 'Já há dedução simplificada aplicada em cálculo de férias anterior nesta competência'
            }
            if (Eventos.valorCalculado(ClassificacaoEvento.DEDUCCOMPLIRRFMENSAL, TipoValor.CALCULADO, TipoProcessamento.FERIAS, SubTipoProcessamento.INTEGRAL, calculo.competencia) > 0) {
                suspender 'Já há dedução legal aplicada em cálculo de férias anterior nesta competência'
            }
        }
        if (TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento)) {
            if (Eventos.valorCalculado(ClassificacaoEvento.DEDUCSIMPLIRRFMENSAL, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.INTEGRAL, calculo.competencia) > 0
                    || Eventos.valorCalculado(ClassificacaoEvento.DEDUCSIMPLIRRFMENSAL, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.COMPLEMENTAR, calculo.competencia) > 0) {
                suspender 'Já há dedução simplificada aplicada em cálculo de mensal anterior nesta competência'
            }
            if (Eventos.valorCalculado(ClassificacaoEvento.DEDUCCOMPLIRRFMENSAL, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.INTEGRAL, calculo.competencia) > 0
                    || Eventos.valorCalculado(ClassificacaoEvento.DEDUCCOMPLIRRFMENSAL, TipoValor.CALCULADO, TipoProcessamento.MENSAL, SubTipoProcessamento.COMPLEMENTAR, calculo.competencia) > 0) {
                suspender 'Já há dedução legal aplicada em cálculo de mensal anterior nesta competência'
            }
        }
        double baseDeducaoIrrf = Bases.valor(Bases.DEDUCIRRFMES)
        if (EncargosSociais.IRRF.deducaoSimplificadaIrrf <= baseDeducaoIrrf) {
            suspender 'O valor da dedução simplificada do IRRF é menor ou igual ao valor das deduções legais. Por este motivo, deve ser aplicada a dedução legal'
        }
        valorCalculado = EncargosSociais.IRRF.deducaoSimplificadaIrrf
    }
    if (TipoProcessamento.FERIAS.equals(calculo.tipoProcessamento)) {
        Bases.compor(valorCalculado, Bases.IRRFFER)
    }
    if (TipoProcessamento.MENSAL.equals(calculo.tipoProcessamento) || TipoProcessamento.RESCISAO.equals(calculo.tipoProcessamento)) {
        Bases.compor(valorCalculado, Bases.IRRF)
    }
}
