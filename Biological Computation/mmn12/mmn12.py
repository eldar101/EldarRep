# Eldar Weiss 303169783 mmn12

import os.path
import sys
import random
import math

## VARS ##
GRID_SIZE = 100  # grid of size nXn

# START = (5 5)  # Maze 1 coordinates
# DESTINATION = (0, 50)  # Maze 1  coordinates

START = (55, 13)  # Maze 2 coordinates
DESTINATION = (40, 80)  # Maze 2 coordinates

MAZE_FILE = "bigmaze2.bmp"

# variables
GENERATIONS = 5000
GENERATIONS_STALEMATE = 500  # Stop after GENERATIOIN_STALEMATE generations without improvement
DNA_SIZE = int(GRID_SIZE * 2.5)
MUTATION = 0.05
CROSSOVER = 0.95
ELITISM = 0.05
POP_SIZE = 60

# Set files
OBSTACLES_MAP = [[False for x in range(GRID_SIZE)] for y in range(GRID_SIZE)]
MAZE_PATH = os.path.join(os.path.dirname(os.path.realpath(__file__)), MAZE_FILE)
SOLVED = False

# arrays
TEST_NAME = "size{}pop{}maze{}".format(GRID_SIZE, POP_SIZE, MAZE_FILE)
STATS_GEN_FITNESS_MAXES = []
STATS_GEN_FITNESS_AVGS = []
STATS_GEN_FITNESS_MINS = []


# distance set
def manhattan_distance(p1, p2):
    "Returns the Manhattan distance between points p1 and p2"
    return abs(p1[0] - p2[0]) + abs(p1[1] - p2[1])


# route info with a tuple
def maze_walk(directions, source=START, destination=DESTINATION):
    if source == destination:
        return 0, 0, 0, source, False

    p = source
    distance = 0
    repeated_nodes = 0
    obstacles = 0
    points_visited = [p]
    for direction in directions:
        distance += 1

        if direction == "L":
            p = (p[0] - 1, p[1])
        elif direction == "R":
            p = (p[0] + 1, p[1])
        elif direction == "U":
            p = (p[0], p[1] - 1)
        elif direction == "D":
            p = (p[0], p[1] + 1)
        else:
            raise ValueError("Direction inapplicable.")

        if p[0] < 0 or p[0] >= GRID_SIZE or p[1] < 0 or p[1] >= GRID_SIZE:
            # illegal move
            return float('inf'), repeated_nodes, obstacles, p, True

        if OBSTACLES_MAP[p[0]][p[1]]:
            obstacles += 1

        if p in points_visited:
            repeated_nodes += 1
        else:
            points_visited.append(p)

        if p == destination:
            return distance, repeated_nodes, obstacles, p, False

    return float('inf'), repeated_nodes, obstacles, p, False


def weighted_choice(items):
    weight_total = sum((item[1] for item in items))
    n = random.uniform(0, weight_total)
    for item, weight in items:
        if n < weight:
            return item
        n -= weight
    return item


# random direction
def random_gene(exclude=None):
    choices = ["U", "D", "L", "R"]

    if exclude:
        choices.remove(exclude)

    return random.choice(choices)


# random chromozome
def random_chromosome():
    return "".join([random_gene() for _ in range(DNA_SIZE)])


# caluclation of fitness
def fitness(dna):
    global SOLVED

    REPEATED_PENALTY = 2  # each repeated node is considered the same as REPEATED_PENALTY steps
    OBSTACLE_PENALTY = 50  # each obstacle on the way is considered the same as OBSTACLE_PENALTY steps
    DEST_NOT_REACHED_PENALTY = manhattan_distance(START, DESTINATION) + DNA_SIZE  # if destination is not reached

    distance, repeated_nodes, obstacles, final_point, is_out_of_bound = maze_walk(dna)

    # Calculate Penalties
    penalties = repeated_nodes * REPEATED_PENALTY + obstacles * OBSTACLE_PENALTY
    if is_out_of_bound:
        penalties = float('inf')
    elif distance == float('inf'):
        penalties += DEST_NOT_REACHED_PENALTY

    if distance == float('inf'):
        weighted_cost = manhattan_distance(final_point, DESTINATION) + penalties
    else:
        weighted_cost = distance + penalties

    if distance != float('inf') and not obstacles:
        SOLVED = True

    # returning the inverse of the cost, so it'll become a proper fitness function
    return 1.0 / weighted_cost


# mutate the board
def mutate(dna):
    output = ""
    for c in dna:
        if random.random() < MUTATION:
            output += random_gene(exclude=c)
        else:
            output += c
    return output


# creates random population
def random_population():
    return [random_chromosome() for _ in range(POP_SIZE)]


def read_obstacles_map():
    mat = read_1bpp_bmp(MAZE_PATH)
    if len(mat) != GRID_SIZE or len(mat[0]) != GRID_SIZE:
        raise ValueError(
            "Maze file size is {}x{}, not our grid size is {}.".format(len(mat), len(mat[0]), GRID_SIZE))
    if mat[START[0]][START[1]]:
        raise ValueError("Starting point {} is in the way of an obstacle.".format(START))
    if mat[DESTINATION[0]][DESTINATION[1]]:
        raise ValueError("Destination point {} is in the way of an obstacle.".format(DESTINATION))
    return mat


# generate obstacle map
def random_obstacles_map(OBSTACLE_RATIO):
    obstacles_left = int(round(OBSTACLE_RATIO * GRID_SIZE ** 2))
    mat = [[False for x in range(GRID_SIZE)] for y in range(GRID_SIZE)]

    while obstacles_left > 0:
        # random x,y
        x, y = random.randint(0, GRID_SIZE - 1), random.randint(0, GRID_SIZE - 1)

        # if the cell is not a source point, dest point, or already has an obstacle, place an obstacle in it
        if not mat[x][y] and (x, y) != START and (x, y) != DESTINATION:
            mat[x][y] = True
            obstacles_left -= 1

    return mat


# single point crossover
def crossover_single_point(dna1, dna2):
    pos = int(random.random() * DNA_SIZE)
    return (dna1[:pos] + dna2[pos:], dna2[:pos] + dna1[pos:])


# Two-point crossover
def crossover_two_point(dna1, dna2):
    return crossover_k_point(dna1, dna2, k=2)


# K-point crossover
def crossover_k_point(dna1, dna2, k):
    for _ in range(k):
        dna1, dna2 = crossover_single_point(dna1, dna2)

    return dna1, dna2


# uniformed crossover
def crossover_uniform(dna1, dna2, p=0.5):
    output_dna1 = ""
    output_dna2 = ""
    for i in range(DNA_SIZE):
        if random.random() < p:
            output_dna1 += dna1[i]
            output_dna2 += dna2[i]
        else:
            output_dna1 += dna2[i]
            output_dna2 += dna1[i]

    return output_dna1, output_dna2


#
def read_1bpp_bmp(path):
    with open(path, 'rb') as f:
        # width
        f.seek(0x12)
        width = f.read(1)[0]
        # height
        f.seek(0x16)
        height = f.read(1)[0]

        mat = [[False for x in range(width)] for y in range(height)]

        f.seek(0x3e)
        for row in range(height):
            # read the num. of bytes required to read the row
            chunk = f.read(int(math.ceil(math.ceil(width / 8.0) / 4) * 4))
            for col in range(width):
                mat[row][col] = (chunk[int(col / 8)] & 2 ** ((7 - col) % 8)) >> ((7 - col) % 8) == 0

    # reverse matrix
    for i in range(int(len(mat) / 2)):
        j = len(mat) - i - 1
        t = mat[i]
        mat[i] = mat[j]
        mat[j] = t

    return mat


# print the matrix
def print_1bpp_bmp_mat(mat):
    sys.stdout.write("\t  {}".format("".join([str(x + 1) for x in range(len(mat[0]))])))
    for x in range(len(mat)):
        sys.stdout.write("\n{}\t".format(x + 1))

        for y in range(len(mat[0])):
            if mat[x][y]:
                sys.stdout.write("*")
            else:
                sys.stdout.write(" ")


def main():
    global OBSTACLES_MAP, SOLVED, STATS_GEN_FITNESS_MAXES, STATS_GEN_FITNESS_AVGS, STATS_GEN_FITNESS_MINS

    # variables and object creation
    population = random_population()
    population.sort(key=lambda x: fitness(x), reverse=True)
    # Random_obstacles_map(0.1)
    OBSTACLES_MAP = read_obstacles_map()
    SOLVED = False

    best_fitness = 0
    same_fitness_count = 0
    for generation in range(0, GENERATIONS):
        # calc statistics
        STATS_GEN_FITNESS_MAXES.append(fitness(population[0]))
        STATS_GEN_FITNESS_MINS.append(fitness(population[-1]))
        STATS_GEN_FITNESS_AVGS.append(float(sum([fitness(x) for x in population])) / len(population))

        # stop
        new_best_fitness = fitness(population[0])
        if best_fitness == new_best_fitness:
            same_fitness_count += 1
        else:
            same_fitness_count = 0

        if same_fitness_count >= GENERATIONS_STALEMATE:
            print("Stopping after {} generations without improvements.".format(same_fitness_count))
            break

        best_fitness = new_best_fitness
        best_fitness_inverted = 1.0 / best_fitness if best_fitness != 0 else float('inf')
        print("Gen {}...\tBest: '{}'\tFitness: (1/{:.2f})\tSolved: {}".format(generation + 1, population[0],
                                                                              best_fitness_inverted, SOLVED))

        weighted_population = [(x, fitness(x)) for x in population]

        # Move ELITISM of the best population to the next generation
        elitism_amount = int(round(ELITISM * POP_SIZE))
        population = population[:elitism_amount]

        for _ in range(int(POP_SIZE / 2)):
            ind1 = weighted_choice(weighted_population)
            ind2 = weighted_choice(weighted_population)

            # Crossover
            if random.random() < CROSSOVER:
                ind1, ind2 = crossover_single_point(ind1, ind2)

            # Mutate and join population
            population.append(mutate(ind1))
            population.append(mutate(ind2))

        # fitness sort
        population.sort(key=lambda x: fitness(x), reverse=True)

        # remove low fitness pop and trip population
        population = population[:POP_SIZE]

    # Print GA performance

    distance, repeated_nodes, obstacles, final_point, is_out_of_bound = maze_walk(population[0])
    best_fitness = fitness(population[0])
    best_fitness_inverted = 1.0 / best_fitness if best_fitness != 0 else float('inf')
    stabilized_generation = generation + 1 - same_fitness_count

    print("*** DONE ***\r\n\r\n")
    print("PATH FOUND:\t\t\t{}\r\n\r\n".format("YES" if distance != float('inf') else "NO"))
    print("Generation: {} (Stabilized at generation {})".format(generation + 1, stabilized_generation + 1))
    print("Best chromosome: '{}'".format(population[0]))
    print("Fitness: {:.2f} (1/{:.2f})".format(best_fitness, best_fitness_inverted))
    print("Distance from destination: {}".format(distance))
    print("repeated_nodes: {}".format(repeated_nodes))
    print("obstacles: {}".format(obstacles))
    print("final_point: {}".format(final_point))
    print("Population Count: {} ({} unique)".format(len(population), len(set(population))))

    return distance != float('inf'), stabilized_generation


if __name__ == "__main__":
    main()
    with open('{}_max.txt'.format(TEST_NAME), 'w') as f:
        for n in STATS_GEN_FITNESS_MAXES: f.write("{}\n".format(n))
    with open('{}_avg.txt'.format(TEST_NAME), 'w') as f:
        for n in STATS_GEN_FITNESS_AVGS: f.write("{}\n".format(n))
    with open('{}_min.txt'.format(TEST_NAME), 'w') as f:
        for n in STATS_GEN_FITNESS_MINS: f.write("{}\n".format(n))
