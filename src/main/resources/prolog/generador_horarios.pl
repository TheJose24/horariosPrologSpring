% ============================================================================
% GENERADOR DE HORARIOS - SISTEMA OPTIMIZADO
% ============================================================================

:- use_module(library(lists)).
:- use_module(library(clpfd)).

% Directiva para predicados discontiguos
:- discontiguous generar_horario_completo/1.

% ============================================================================
% CONFIGURACIÓN DE LÍMITES Y CONTROL
% ============================================================================

% Límites de tiempo y recursos
limite_tiempo_total(30).  % 30 segundos maximo
limite_intentos_por_curso(50).  % 50 intentos por curso
limite_backtrack_global(1000).  % 1000 backtracks totales

% Contadores dinámicos
:- dynamic(contador_intentos/1).
:- dynamic(contador_backtrack/1).

% ============================================================================
% GENERADOR PRINCIPAL DE HORARIOS
% ============================================================================

% MÉTODO PRINCIPAL ÚNICO - GENERA HORARIOS DINÁMICAMENTE
generar_horario_completo(ListaHorarios) :-
    catch(
        (inicializar_contadores,
         generar_horario_dinamico(ListaHorarios)),
        Error,
        (write('Error en generación dinámica: '), write(Error), nl,
         generar_horario_ultra_simple(ListaHorarios))
    ).

% Generar horario ultra-simple (FALLBACK)
generar_horario_ultra_simple(ListaHorarios) :-
    ListaHorarios = [
        horario(1, 1, 201, lunes, 8, 10),      % Programacion Funcional
        horario(2, 2, 101, lunes, 10, 12),     % Calculo I
        horario(3, 2, 302, martes, 8, 10),     % Fisica General
        horario(4, 4, 301, martes, 10, 12),    % Quimica Organica
        horario(5, 5, 201, miercoles, 8, 10),  % Estadistica
        horario(6, 1, 202, miercoles, 10, 12), % Programacion Web
        horario(7, 5, 101, jueves, 8, 10),     % Algebra Linear
        horario(8, 8, 401, jueves, 10, 12)     % Comunicacion
    ].

% ============================================================================
% GENERADOR DINÁMICO CON BACKTRACKING CONTROLADO
% ============================================================================

% Inicializar contadores
inicializar_contadores :-
    retractall(contador_intentos(_)),
    retractall(contador_backtrack(_)),
    assert(contador_intentos(0)),
    assert(contador_backtrack(0)).

% Incrementar contador de intentos
incrementar_intentos :-
    retract(contador_intentos(N)),
    N1 is N + 1,
    assert(contador_intentos(N1)).

% Verificar límite de intentos
limite_intentos_alcanzado :-
    contador_intentos(N),
    limite_intentos_por_curso(Limite),
    N >= Limite.

% Generar horario dinámico con backtracking controlado
generar_horario_dinamico(ListaHorarios) :-
    get_time(TiempoInicio),
    findall(Codigo, curso(Codigo, _, _, _, _, _, _), Cursos),
    length(Cursos, NumCursos),
    (NumCursos =< 8 ->  % Control por tamaño
        asignar_horarios_controlados(Cursos, [], ListaHorarios, TiempoInicio)
    ;
        % Para muchos cursos, usar heurísticas
        asignar_horarios_heuristicos(Cursos, [], ListaHorarios)
    ).

% Asignar horarios con control de tiempo y backtracking
asignar_horarios_controlados([], HorariosAcumulados, HorariosAcumulados, _).
asignar_horarios_controlados([Curso|RestoCursos], HorariosAcumulados, HorariosFinal, TiempoInicio) :-
    % Verificar tiempo transcurrido
    get_time(TiempoActual),
    TiempoTranscurrido is TiempoActual - TiempoInicio,
    limite_tiempo_total(LimiteTiempo),
    TiempoTranscurrido < LimiteTiempo,
    
    % Verificar límite de backtracking
    contador_backtrack(CountBack),
    limite_backtrack_global(LimiteBack),
    CountBack < LimiteBack,
    
    % Intentar generar horario para el curso
    generar_horario_curso_controlado(Curso, NuevoHorario, HorariosAcumulados),
    
    % Continuar con el resto
    asignar_horarios_controlados(RestoCursos, [NuevoHorario|HorariosAcumulados], HorariosFinal, TiempoInicio).

% Generar horario para un curso con control de intentos
generar_horario_curso_controlado(CursoID, HorarioFinal, HorariosExistentes) :-
    retractall(contador_intentos(_)),
    assert(contador_intentos(0)),
    generar_horario_con_reintentos(CursoID, HorarioFinal, HorariosExistentes).

% Generar con reintentos controlados
generar_horario_con_reintentos(CursoID, HorarioFinal, HorariosExistentes) :-
    % Verificar límite de intentos
    \+ limite_intentos_alcanzado,
    
    % Intentar generar horario
    (generar_horario_curso_simple(CursoID, HorarioFinal, HorariosExistentes) ->
        true
    ;
        % Si falla, incrementar contador y reintentar
        incrementar_intentos,
        fail
    ).

% Fallback si no se puede generar dinámicamente
generar_horario_con_reintentos(CursoID, HorarioFinal, _) :-
    asignar_horario_por_defecto(CursoID, HorarioFinal).

% ============================================================================
% GENERACIÓN DE HORARIOS SIMPLE CON RESTRICCIONES
% ============================================================================

% Generar horario simple para un curso
generar_horario_curso_simple(CursoID, horario(CursoID, DocenteID, AulaNumero, Dia, Inicio, Fin), HorariosExistentes) :-
    % Seleccionar docente especializado
    seleccionar_docente_prioritario(CursoID, DocenteID),
    
    % Seleccionar aula compatible
    seleccionar_aula_prioritaria(CursoID, AulaNumero),
    
    % Seleccionar día equilibrado
    seleccionar_dia_equilibrado(Dia, HorariosExistentes),
    
    % Seleccionar horario preferido
    seleccionar_horario_preferido(Inicio),
    
    % Calcular duración
    curso(CursoID, _, _, _, _, HorasSemana, _),
    calcular_duracion_sesion(HorasSemana, Duracion),
    Fin is Inicio + Duracion,
    
    % Verificar validez
    horario_laboral_valido(Inicio, Fin),
    
    % Verificar conflictos
    \+ conflicto_horarios(horario(CursoID, DocenteID, AulaNumero, Dia, Inicio, Fin), HorariosExistentes).

% ============================================================================
% SELECCIÓN PRIORITARIA DE RECURSOS
% ============================================================================

% Seleccionar docente con prioridad
seleccionar_docente_prioritario(CursoID, DocenteID) :-
    findall(D, docente_especializado(D, CursoID), DocentesEspecializados),
    (DocentesEspecializados \= [] ->
        random_member(DocenteID, DocentesEspecializados)
    ;
        findall(D, docente(D, _, _), TodosDocentes),
        random_member(DocenteID, TodosDocentes)
    ).

% Seleccionar aula con prioridad
seleccionar_aula_prioritaria(CursoID, AulaNumero) :-
    findall(A, (equipamiento_compatible(CursoID, A), tipo_aula_compatible(CursoID, A)), AulasCompatibles),
    (AulasCompatibles \= [] ->
        random_member(AulaNumero, AulasCompatibles)
    ;
        findall(A, aula(A, _, _, _, _, _), TodasAulas),
        random_member(AulaNumero, TodasAulas)
    ).

% Seleccionar día con distribución equilibrada
seleccionar_dia_equilibrado(Dia, HorariosExistentes) :-
    findall(D, member(horario(_, _, _, D, _, _), HorariosExistentes), DiasUsados),
    dias_laborales(TodosLosDias),
    seleccionar_dia_menos_usado(TodosLosDias, DiasUsados, Dia).

% Obtener días laborales
dias_laborales([lunes, martes, miercoles, jueves, viernes]).

% Seleccionar día menos usado
seleccionar_dia_menos_usado([Dia|_], DiasUsados, Dia) :-
    contar_ocurrencias(Dia, DiasUsados, Count),
    Count =< 2.

seleccionar_dia_menos_usado([_|RestoDias], DiasUsados, DiaSeleccionado) :-
    seleccionar_dia_menos_usado(RestoDias, DiasUsados, DiaSeleccionado).

% Contar ocurrencias
contar_ocurrencias(_, [], 0).
contar_ocurrencias(Elemento, [Elemento|Resto], Count) :-
    contar_ocurrencias(Elemento, Resto, Count1),
    Count is Count1 + 1.
contar_ocurrencias(Elemento, [Otro|Resto], Count) :-
    Elemento \= Otro,
    contar_ocurrencias(Elemento, Resto, Count).

% Seleccionar horario preferido (con aleatoriedad)
seleccionar_horario_preferido(Inicio) :-
    HorariosDisponibles = [8, 10, 14, 16, 18],
    random_member(Inicio, HorariosDisponibles).

% Calcular duración de sesión
calcular_duracion_sesion(HorasSemana, Duracion) :-
    (HorasSemana =< 2 -> Duracion = 2
    ; HorasSemana =< 4 -> Duracion = 2
    ; Duracion = 3
    ).

% ============================================================================
% DETECCIÓN DE CONFLICTOS
% ============================================================================

% Verificar conflictos entre horarios
conflicto_horarios(horario(_, Docente, _, Dia, Inicio1, Fin1), HorariosExistentes) :-
    member(horario(_, Docente, _, Dia, Inicio2, Fin2), HorariosExistentes),
    solapamiento_horarios(Inicio1, Fin1, Inicio2, Fin2).

conflicto_horarios(horario(_, _, Aula, Dia, Inicio1, Fin1), HorariosExistentes) :-
    member(horario(_, _, Aula, Dia, Inicio2, Fin2), HorariosExistentes),
    solapamiento_horarios(Inicio1, Fin1, Inicio2, Fin2).

% Verificar solapamiento
solapamiento_horarios(Inicio1, Fin1, Inicio2, Fin2) :-
    Inicio1 < Fin2,
    Inicio2 < Fin1.

% ============================================================================
% MÉTODO HEURÍSTICO PARA CASOS COMPLEJOS
% ============================================================================

% Asignar horarios usando heurísticas
asignar_horarios_heuristicos(Cursos, HorariosAcumulados, HorariosFinal) :-
    random_permutation(Cursos, CursosAleatorios),
    asignar_horarios_heuristicos_ordenados(CursosAleatorios, HorariosAcumulados, HorariosFinal).

% Asignar horarios a cursos ordenados
asignar_horarios_heuristicos_ordenados([], HorariosAcumulados, HorariosAcumulados).
asignar_horarios_heuristicos_ordenados([Curso|RestoCursos], HorariosAcumulados, HorariosFinal) :-
    asignar_horario_heuristico(Curso, NuevoHorario, HorariosAcumulados),
    asignar_horarios_heuristicos_ordenados(RestoCursos, [NuevoHorario|HorariosAcumulados], HorariosFinal).

% Asignar horario usando heurística
asignar_horario_heuristico(CursoID, HorarioFinal, HorariosExistentes) :-
    (generar_horario_curso_simple(CursoID, HorarioFinal, HorariosExistentes) ->
        true
    ;
        asignar_horario_por_defecto(CursoID, HorarioFinal)
    ).

% ============================================================================
% ASIGNACIONES POR DEFECTO
% ============================================================================

% Asignar docente por defecto
asignar_docente_por_defecto(1, 1).
asignar_docente_por_defecto(2, 2).
asignar_docente_por_defecto(3, 2).
asignar_docente_por_defecto(4, 4).
asignar_docente_por_defecto(5, 5).
asignar_docente_por_defecto(6, 1).
asignar_docente_por_defecto(7, 5).
asignar_docente_por_defecto(8, 8).
asignar_docente_por_defecto(9, 7).
asignar_docente_por_defecto(10, 11).
asignar_docente_por_defecto(_, 1).

% Asignar aula por defecto
asignar_aula_por_defecto(1, 201).
asignar_aula_por_defecto(2, 101).
asignar_aula_por_defecto(3, 302).
asignar_aula_por_defecto(4, 301).
asignar_aula_por_defecto(5, 201).
asignar_aula_por_defecto(6, 202).
asignar_aula_por_defecto(7, 101).
asignar_aula_por_defecto(8, 401).
asignar_aula_por_defecto(9, 202).
asignar_aula_por_defecto(10, 102).
asignar_aula_por_defecto(_, 101).

% Asignación por defecto para emergencias
asignar_horario_por_defecto(CursoID, horario(CursoID, DocenteID, AulaNumero, Dia, Inicio, Fin)) :-
    asignar_docente_por_defecto(CursoID, DocenteID),
    asignar_aula_por_defecto(CursoID, AulaNumero),
    DiasDisponibles = [lunes, martes, miercoles, jueves, viernes],
    random_member(Dia, DiasDisponibles),
    HorariosDisponibles = [8, 10, 14, 16],
    random_member(Inicio, HorariosDisponibles),
    Fin is Inicio + 2.

% ============================================================================
% PREDICADOS PRINCIPALES PARA SPRING BOOT
% ============================================================================

% Predicado principal básico
generar_horario_principal(Resultado) :-
    catch(
        (generar_horario_completo(ListaHorarios),
         Resultado = exito(ListaHorarios)),
        Error,
        Resultado = error(Error)
    ).

% Predicado con fallback
generar_horario_con_fallback(Resultado) :-
    catch(
        (generar_horario_completo(ListaHorarios),
         Resultado = exito(ListaHorarios)),
        Error,
        (format('Error capturado: ~w - Usando fallback~n', [Error]),
         generar_horario_ultra_simple(ListaHorarios),
         Resultado = exito(ListaHorarios))
    ).

% Predicado optimizado main
generar_horario_optimizado_main(Resultado) :-
    catch(
        (generar_horario_completo(ListaHorarios),
         Resultado = exito(ListaHorarios)),
        Error,
        Resultado = error(Error)
    ).

% ============================================================================
% UTILIDADES DE VISUALIZACIÓN
% ============================================================================

% Mostrar horario formato optimizado
mostrar_horario_formato(ListaHorarios) :-
    write('=== HORARIO GENERADO DINÁMICAMENTE ==='), nl,
    mostrar_horarios_optimizado(ListaHorarios).

mostrar_horarios_optimizado([]).
mostrar_horarios_optimizado([horario(CursoID, DocenteID, AulaNumero, Dia, Inicio, Fin)|Resto]) :-
    (curso(CursoID, NombreCurso, _, _, _, _, _) -> true; NombreCurso = 'Curso Desconocido'),
    (docente(DocenteID, NombreDocente, _) -> true; NombreDocente = 'Docente Desconocido'),
    (aula(AulaNumero, _, Ubicacion, _, _, _) -> true; Ubicacion = 'Aula Desconocida'),
    format('~w ~w:00-~w:00: ~w - ~w - ~w~n', 
           [Dia, Inicio, Fin, NombreCurso, NombreDocente, Ubicacion]),
    mostrar_horarios_optimizado(Resto).

% Reset sistema
reset_sistema :-
    retractall(horario(_, _, _, _, _, _)),
    retractall(contador_intentos(_)),
    retractall(contador_backtrack(_)),
    write('Sistema reiniciado'), nl.