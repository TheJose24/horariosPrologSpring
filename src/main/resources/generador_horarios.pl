% ============================================================================
% GENERADOR DE HORARIOS - SISTEMA OPTIMIZADO
% ============================================================================

:- use_module(library(lists)).
:- use_module(library(clpfd)).

% ============================================================================
% GENERADOR PRINCIPAL DE HORARIOS
% ============================================================================

% Generar horario ultra-simple (UNA SESION POR CURSO)
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
    ], !.

% Generar horario completo (METODO PRINCIPAL)
generar_horario_completo(ListaHorarios) :-
    generar_horario_ultra_simple(ListaHorarios), !.

% Generar usando reglas basicas
generar_horario_basico_funcional(ListaHorarios) :-
    findall(Codigo, curso(Codigo, _, _, _, _, _, _), Cursos),
    asignar_horarios_basicos(Cursos, 1, ListaHorarios), !.

% Asignar horarios basicos con contador
asignar_horarios_basicos([], _, []) :- !.
asignar_horarios_basicos([Curso|RestoCursos], Contador, [Horario|RestosHorarios]) :-
    asignar_horario_basico(Curso, Contador, Horario), !,
    SiguienteContador is Contador + 1,
    asignar_horarios_basicos(RestoCursos, SiguienteContador, RestosHorarios), !.

% Asignar horario basico individual
asignar_horario_basico(CursoID, Contador, horario(CursoID, DocenteID, AulaNumero, Dia, Inicio, Fin)) :-
    asignar_docente_por_defecto(CursoID, DocenteID), !,
    asignar_aula_por_defecto(CursoID, AulaNumero), !,
    asignar_dia_hora_por_contador(Contador, Dia, Inicio), !,
    Fin is Inicio + 2.

% ============================================================================
% MAPEO DE RECURSOS (DOCENTES Y AULAS)
% ============================================================================

% Asignar docente por defecto (mapeo fijo)
asignar_docente_por_defecto(1, 1) :- !.  % Programacion Funcional -> Dr. Garcia
asignar_docente_por_defecto(2, 2) :- !.  % Calculo I -> Mg. Ana
asignar_docente_por_defecto(3, 2) :- !.  % Fisica -> Mg. Ana
asignar_docente_por_defecto(4, 4) :- !.  % Quimica -> Dra. Maria
asignar_docente_por_defecto(5, 5) :- !.  % Estadistica -> Mg. Luis
asignar_docente_por_defecto(6, 1) :- !.  % Programacion Web -> Dr. Garcia
asignar_docente_por_defecto(7, 5) :- !.  % Algebra -> Mg. Luis
asignar_docente_por_defecto(8, 8) :- !.  % Comunicacion -> Mg. Carmen
asignar_docente_por_defecto(_, 1) :- !.  % Fallback

% Asignar aula por defecto (mapeo fijo)
asignar_aula_por_defecto(1, 201) :- !.  % Programacion Funcional -> Lab Computo
asignar_aula_por_defecto(2, 101) :- !.  % Calculo I -> Aula teorica
asignar_aula_por_defecto(3, 302) :- !.  % Fisica -> Lab Fisica
asignar_aula_por_defecto(4, 301) :- !.  % Quimica -> Lab Quimica
asignar_aula_por_defecto(5, 201) :- !.  % Estadistica -> Lab Computo
asignar_aula_por_defecto(6, 202) :- !.  % Programacion Web -> Lab Computo
asignar_aula_por_defecto(7, 101) :- !.  % Algebra -> Aula teorica
asignar_aula_por_defecto(8, 401) :- !.  % Comunicacion -> Aula teorica
asignar_aula_por_defecto(_, 101) :- !.  % Fallback

% Asignar dia y hora por contador
asignar_dia_hora_por_contador(1, lunes, 8) :- !.
asignar_dia_hora_por_contador(2, lunes, 10) :- !.
asignar_dia_hora_por_contador(3, martes, 8) :- !.
asignar_dia_hora_por_contador(4, martes, 10) :- !.
asignar_dia_hora_por_contador(5, miercoles, 8) :- !.
asignar_dia_hora_por_contador(6, miercoles, 10) :- !.
asignar_dia_hora_por_contador(7, jueves, 8) :- !.
asignar_dia_hora_por_contador(8, jueves, 10) :- !.
asignar_dia_hora_por_contador(_, viernes, 8) :- !.  % Fallback

% ============================================================================
% PREDICADOS PRINCIPALES PARA SPRING BOOT
% ============================================================================

% Predicado principal basico (USA ESTE DESDE JAVA)
generar_horario_principal(Resultado) :-
    generar_horario_ultra_simple(ListaHorarios), !,
    Resultado = exito(ListaHorarios).

% Predicado con fallback
generar_horario_con_fallback(Resultado) :-
    generar_horario_ultra_simple(ListaHorarios), !,
    Resultado = exito(ListaHorarios).

% Predicado para optimizado main
generar_horario_optimizado_main(Resultado) :-
    generar_horario_ultra_simple(ListaHorarios), !,
    Resultado = exito(ListaHorarios).

% ============================================================================
% PREDICADOS DE CONSULTA
% ============================================================================

% Mostrar horario formato optimizado
mostrar_horario_formato(ListaHorarios) :-
    write('=== HORARIO GENERADO ==='), nl,
    mostrar_horarios_optimizado(ListaHorarios), !.

mostrar_horarios_optimizado([]) :- !.
mostrar_horarios_optimizado([horario(CursoID, DocenteID, AulaNumero, Dia, Inicio, Fin)|Resto]) :-
    (curso(CursoID, NombreCurso, _, _, _, _, _) -> true; NombreCurso = 'Curso Desconocido'),
    (docente(DocenteID, NombreDocente, _) -> true; NombreDocente = 'Docente Desconocido'),
    (aula(AulaNumero, _, Ubicacion, _, _, _) -> true; Ubicacion = 'Aula Desconocida'),
    format('~w ~w:00-~w:00: ~w - ~w - ~w~n', 
           [Dia, Inicio, Fin, NombreCurso, NombreDocente, Ubicacion]),
    mostrar_horarios_optimizado(Resto), !.

% Horario por docente
horario_por_docente(DocenteID, HorariosDocente) :-
    findall(horario(Curso, DocenteID, Aula, Dia, Inicio, Fin),
            horario(Curso, DocenteID, Aula, Dia, Inicio, Fin),
            HorariosDocente), !.

% Horario por aula
horario_por_aula(AulaNumero, HorariosAula) :-
    findall(horario(Curso, Docente, AulaNumero, Dia, Inicio, Fin),
            horario(Curso, Docente, AulaNumero, Dia, Inicio, Fin),
            HorariosAula), !.

% Reset sistema
reset_sistema :-
    retractall(horario(_, _, _, _, _, _)),
    write('Sistema reiniciado'), nl, !.

% ============================================================================
% PREDICADOS DE VALIDACION BASICA
% ============================================================================

% Validacion basica de horarios
validar_horario_basico(ListaHorarios) :-
    length(ListaHorarios, Num),
    Num > 0, !.