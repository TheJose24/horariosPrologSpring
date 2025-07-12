% ============================================================================
% VALIDACIONES - SISTEMA DE HORARIOS
% ============================================================================

:- use_module(library(lists)).
:- use_module(library(clpfd)).

% ============================================================================
% VALIDACIONES DE ESPECIALIDAD Y EQUIPAMIENTO
% ============================================================================

% Verificar que el docente esta especializado para el curso
docente_especializado(DocenteID, CursoID) :-
    docente(DocenteID, _, Especialidades),
    curso(CursoID, _, _, _, _, _, _),
    especialidad_requerida_curso(CursoID, EspecialidadRequerida),
    tiene_especialidad(EspecialidadRequerida, Especialidades), !.

% Predicado auxiliar para verificar especialidad con cut
tiene_especialidad(Especialidad, [Especialidad|_]) :- !.
tiene_especialidad(Especialidad, [_|Resto]) :-
    tiene_especialidad(Especialidad, Resto).

% Definir especialidades requeridas por curso
especialidad_requerida_curso(1, informatica).  % Programacion Funcional
especialidad_requerida_curso(2, matematicas).  % Calculo I
especialidad_requerida_curso(3, fisica).       % Fisica General
especialidad_requerida_curso(4, quimica).      % Quimica Organica
especialidad_requerida_curso(5, estadistica).  % Estadistica
especialidad_requerida_curso(6, informatica).  % Programacion Web
especialidad_requerida_curso(7, matematicas).  % Algebra Linear
especialidad_requerida_curso(8, comunicacion). % Comunicacion

% Verificar compatibilidad de equipamiento
equipamiento_compatible(CursoID, AulaNumero) :-
    curso(CursoID, _, _, _, _, _, EquipamientoRequerido),
    aula(AulaNumero, _, _, _, EquipamientoDisponible, _),
    equipamiento_suficiente(EquipamientoRequerido, EquipamientoDisponible), !.

% Verificar que el aula tiene todo el equipamiento requerido
equipamiento_suficiente([], _) :- !.
equipamiento_suficiente([Equipo|Resto], EquipamientoDisponible) :-
    tiene_equipamiento(Equipo, EquipamientoDisponible), !,
    equipamiento_suficiente(Resto, EquipamientoDisponible).

% Predicado auxiliar para verificar equipamiento con cut
tiene_equipamiento(Equipo, [Equipo|_]) :- !.
tiene_equipamiento(Equipo, [_|Resto]) :-
    tiene_equipamiento(Equipo, Resto).

% ============================================================================
% VALIDACIONES DE TIPO DE AULA
% ============================================================================

% Verificar que el tipo de aula es compatible con el tipo de sesion
tipo_aula_compatible(CursoID, AulaNumero) :-
    curso(CursoID, _, _, _, TipoSesion, _, _),
    aula(AulaNumero, _, _, _, _, TipoAula),
    compatible_sesion_aula(TipoSesion, TipoAula), !.

% Definir compatibilidad entre tipos de sesion y aulas
compatible_sesion_aula(teorico, teorica) :- !.
compatible_sesion_aula(teorico, laboratorio_computo) :- !.
compatible_sesion_aula(practico, laboratorio_computo) :- !.
compatible_sesion_aula(practico, laboratorio_quimica) :- !.
compatible_sesion_aula(practico, laboratorio_fisica) :- !.
compatible_sesion_aula(teorico_practico, laboratorio_computo) :- !.
compatible_sesion_aula(teorico_practico, laboratorio_quimica) :- !.
compatible_sesion_aula(teorico_practico, laboratorio_fisica) :- !.

% ============================================================================
% VALIDACIONES BASICAS
% ============================================================================

% Verificar que el dia es laboral
dia_laboral(lunes).
dia_laboral(martes).
dia_laboral(miercoles).
dia_laboral(jueves).
dia_laboral(viernes).
dia_laboral(sabado).

% Verificar que el horario esta dentro del horario laboral (8:00 - 22:00)
horario_laboral_valido(Inicio, Fin) :-
    Inicio >= 8,
    Fin =< 22,
    Inicio < Fin.

% Verificar duracion minima y maxima de sesion
duracion_sesion_valida(Inicio, Fin) :-
    Duracion is Fin - Inicio,
    Duracion >= 1,  % Minimo 1 hora
    Duracion =< 2.  % Maximo 2 horas

% ============================================================================
% VALIDACION COMPLETA DE HORARIO
% ============================================================================

% Validar un horario individual
validar_horario_individual(horario(CursoID, DocenteID, AulaNumero, Dia, Inicio, Fin)) :-
    curso(CursoID, _, _, _, _, _, _), !,
    docente(DocenteID, _, _), !,
    aula(AulaNumero, _, _, _, _, _), !,
    dia_laboral(Dia), !,
    horario_laboral_valido(Inicio, Fin), !,
    duracion_sesion_valida(Inicio, Fin), !,
    docente_especializado(DocenteID, CursoID), !,
    equipamiento_compatible(CursoID, AulaNumero), !,
    tipo_aula_compatible(CursoID, AulaNumero), !.

% Validar una lista completa de horarios
validar_horario(ListaHorarios) :-
    validar_horarios_individuales(ListaHorarios).

% Validar cada horario individual en la lista
validar_horarios_individuales([]).
validar_horarios_individuales([Horario|Resto]) :-
    validar_horario_individual(Horario),
    validar_horarios_individuales(Resto).

% Generar reporte basico de validacion
generar_reporte_validacion(ListaHorarios, Reporte) :-
    length(ListaHorarios, NumHorarios),
    Reporte = reporte_basico(NumHorarios).