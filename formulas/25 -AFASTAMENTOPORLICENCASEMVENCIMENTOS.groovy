def classificacoesAfastamentos = [
        ClassificacaoTipoAfastamento.LICENCA_SEM_VENCIMENTOS
]
Boolean existeAfastamento = false;
for (classificacaoAfastamento in classificacoesAfastamentos){
    if ( existeAfastamento ) continue;
    existeAfastamento = afastamento.existeAfastamento(classificacaoAfastamento, apuracao.dataApuracaoFormatado)
}
valorApurado = 0;
if ( existeAfastamento ) valorApurado = horario.cargaHoraria;
