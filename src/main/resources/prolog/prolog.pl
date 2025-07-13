% ============================================================================
% SISTEMA DE HORARIOS - ARCHIVO PRINCIPAL
% ============================================================================

% Cargar modulos base
:- consult('base_conocimiento.pl').
:- consult('validaciones.pl').
:- consult('generador_horarios.pl').

% Directivas para optimizacion
:- set_prolog_flag(debug, off).

% ============================================================================
% PREDICADOS PRINCIPALES DE INTERFAZ
% ============================================================================

% Inicializar sistema completo
inicializar_sistema_completo :-
    write('Inicializando Sistema de Horarios...'), nl,
    inicializar_sistema,
    write('Sistema listo para usar.'), nl.

% Generar horario completo con reporte
generar_horario_con_reporte(Resultado) :-
    catch(
        (generar_horario_completo(ListaHorarios),
         generar_reporte_validacion(ListaHorarios, Reporte),
         Resultado = exito(ListaHorarios, Reporte)),
        Error,
        Resultado = error(Error)
    ).

% ============================================================================
% PREDICADO PRINCIPAL PARA SPRING BOOT
% ============================================================================

main_generar_horario(TipoAlgoritmo, Resultado) :-
    (TipoAlgoritmo = 'CLPFD' ->
        generar_horario_optimizado_main(Resultado)
    ; TipoAlgoritmo = 'HIBRIDO' ->
        generar_horario_con_fallback(Resultado)
    ; 
        generar_horario_principal(Resultado)
    ).

% ============================================================================
% PREDICADOS DE CONSULTA AVANZADA
% ============================================================================

% Obtener estadisticas completas
obtener_estadisticas_completas(Estadisticas) :-
    total_docentes(Docentes),
    total_cursos(Cursos),
    total_aulas(Aulas),
    findall(H, horario(H, _, _, _, _, _), HorariosActuales),
    length(HorariosActuales, TotalHorarios),
    Estadisticas = estadisticas(Docentes, Cursos, Aulas, TotalHorarios).

% Buscar horarios con filtros
buscar_horarios_filtrados(Filtros, Resultados) :-
    findall(horario(C, D, A, Dia, I, F), 
            (horario(C, D, A, Dia, I, F),
             aplicar_filtros(horario(C, D, A, Dia, I, F), Filtros)),
            Resultados).

aplicar_filtros(_, []).
aplicar_filtros(Horario, [Filtro|RestoFiltros]) :-
    aplicar_filtro(Horario, Filtro),
    aplicar_filtros(Horario, RestoFiltros).

aplicar_filtro(horario(_, DocenteID, _, _, _, _), docente(DocenteID)).
aplicar_filtro(horario(CursoID, _, _, _, _, _), curso(CursoID)).
aplicar_filtro(horario(_, _, AulaNumero, _, _, _), aula(AulaNumero)).
aplicar_filtro(horario(_, _, _, Dia, _, _), dia(Dia)).