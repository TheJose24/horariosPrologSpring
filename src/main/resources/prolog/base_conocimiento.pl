% ============================================================================
% BASE DE CONOCIMIENTO - SISTEMA DE HORARIOS
% ============================================================================

% Directivas para modulos necesarios
:- use_module(library(lists)).
:- use_module(library(clpfd)).

% Predicados dinamicos para permitir modificaciones en tiempo de ejecucion
:- dynamic(docente/3).
:- dynamic(curso/7).
:- dynamic(aula/6).
:- dynamic(horario/6).

% Directivas para predicados discontiguos (si es necesario)
:- discontiguous docente/3.
:- discontiguous curso/7.

% ============================================================================
% DATOS DE DOCENTES (TODOS AGRUPADOS)
% ============================================================================
% Estructura: docente(ID, NombreCompleto, ListaEspecialidades)

docente(1, 'Garcia Lopez', [informatica, matematicas]).
docente(2, 'Ana Rodriguez', [fisica, matematicas]).
docente(3, 'Carlos Mendoza', [informatica, sistemas]).
docente(4, 'Maria Santos', [quimica, biologia]).
docente(5, 'Luis Herrera', [matematicas, estadistica]).
docente(6, 'Patricia Vega', [fisica, matematicas]).
docente(7, 'Roberto Silva', [informatica, redes]).
docente(8, 'Carmen Torres', [literatura, comunicacion]).
docente(9, 'Jose Martinez', [biologia, quimica]).
docente(10, 'Ana Gutierrez', [matematicas, estadistica]).
docente(11, 'Juan Perez', [matematicas, fisica]).
docente(12, 'Miguel Grau', [matematicas]).

% ============================================================================
% DATOS DE CURSOS (TODOS AGRUPADOS)
% ============================================================================
% Estructura: curso(CodigoCurso, NombreCurso, Ciclo, Naturaleza, TipoSesion, HorasSemana, EquipamientoNecesario)

curso(1, 'Programacion Funcional', 4, carrera, teorico_practico, 4, [computadoras, internet]).
curso(2, 'Calculo I', 1, general, teorico, 3, [proyector, pizarra]).
curso(3, 'Fisica General', 2, general, teorico_practico, 4, [proyector, laboratorio_fisica]).
curso(4, 'Quimica Organica', 3, carrera, practico, 2, [laboratorio_quimica, ventilacion]).
curso(5, 'Estadistica', 2, general, teorico, 3, [proyector, computadoras]).
curso(6, 'Programacion Web', 5, carrera, teorico_practico, 4, [computadoras, internet]).
curso(7, 'Algebra Linear', 2, general, teorico, 2, [proyector, pizarra]).
curso(8, 'Comunicacion', 1, general, teorico, 2, [proyector, audio]).
curso(9, 'Redes II', 8, carrera, practico, 4, [computadoras, equipos_redes]).
curso(10, 'Matematica II', 4, general, teorico, 4, [proyector, pizarra]).

% ============================================================================
% DATOS DE AULAS
% ============================================================================
% Estructura: aula(NumeroAula, Piso, Ubicacion, Capacidad, EquipamientoDisponible, TipoAula)

aula(101, 1, 'A-101', 40, [proyector, pizarra, audio], teorica).
aula(102, 1, 'A-102', 35, [proyector, pizarra], teorica).
aula(201, 2, 'B-201', 30, [proyector, computadoras, internet], laboratorio_computo).
aula(202, 2, 'B-202', 30, [proyector, computadoras, internet], laboratorio_computo).
aula(301, 3, 'C-301', 25, [laboratorio_quimica, ventilacion, agua], laboratorio_quimica).
aula(302, 3, 'C-302', 25, [laboratorio_fisica, equipos_medicion], laboratorio_fisica).
aula(401, 4, 'D-401', 50, [proyector, pizarra, audio], teorica).
aula(402, 4, 'D-402', 45, [proyector, pizarra], teorica).

% ============================================================================
% PREDICADOS DE UTILIDAD
% ============================================================================

% Contar total de docentes
total_docentes(Total) :-
    findall(ID, docente(ID, _, _), Lista),
    length(Lista, Total).

% Contar total de cursos
total_cursos(Total) :-
    findall(Codigo, curso(Codigo, _, _, _, _, _, _), Lista),
    length(Lista, Total).

% Contar total de aulas
total_aulas(Total) :-
    findall(Numero, aula(Numero, _, _, _, _, _), Lista),
    length(Lista, Total).

% Limpiar horarios generados
limpiar_horarios :-
    retractall(horario(_, _, _, _, _, _)).

% ============================================================================
% GENERACIÓN AUTOMÁTICA DE HORARIOS (EJEMPLO BÁSICO)
% ============================================================================
% Este predicado limpia los horarios anteriores y genera un horario para cada curso con el primer docente disponible y la primera aula disponible, en lunes de 8 a 10.

generar_horarios :-
    retractall(horario(_, _, _, _, _, _)),
    forall(
        (curso(CodCurso, _, _, _, _, _, _), docente(IdDocente, _, _), aula(NumAula, _, _, _, _, _)),
        assertz(horario(CodCurso, IdDocente, NumAula, lunes, 8, 10))
    ).

% ============================================================================
% PREDICADOS DE INICIALIZACION
% ============================================================================

% Inicializar sistema
inicializar_sistema :-
    write('Sistema de Horarios inicializado correctamente'), nl,
    total_docentes(Docentes),
    total_cursos(Cursos),
    total_aulas(Aulas),
    format('Datos cargados: ~w docentes, ~w cursos, ~w aulas~n', [Docentes, Cursos, Aulas]).

% Mostrar estadisticas del sistema
mostrar_estadisticas :-
    total_docentes(Docentes),
    total_cursos(Cursos),
    total_aulas(Aulas),
    format('=== ESTADISTICAS DEL SISTEMA ===~n'),
    format('Docentes registrados: ~w~n', [Docentes]),
    format('Cursos registrados: ~w~n', [Cursos]),
    format('Aulas disponibles: ~w~n', [Aulas]),
    format('================================~n').
docente(14, 'messi', [matematicas]).
curso(15, 'messi', 9, especialidad, teorico_practico, 4, [proyector]).
docente(22, 'susy diaz', [quimica]).
curso(10, 'sssss', 10, especialidad, teorico_practico, 4, [proyector]).
horario(1, 1, 101, lunes, 8, 10).
horario(2, 2, 101, lunes, 8, 10).
horario(3, 3, 101, lunes, 8, 10).
horario(4, 4, 101, lunes, 8, 10).
horario(5, 5, 101, lunes, 8, 10).
horario(6, 6, 101, lunes, 8, 10).
horario(7, 7, 101, lunes, 8, 10).
horario(8, 8, 101, lunes, 8, 10).
horario(9, 9, 101, lunes, 8, 10).
horario(10, 10, 101, lunes, 8, 10).
horario(15, 11, 101, lunes, 8, 10).
horario(10, 12, 101, lunes, 8, 10).
