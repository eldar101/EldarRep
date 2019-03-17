# Objects for compilation
SRCS =      Constants.o \
			Error.o \
			Parser.o \
			StringHelper.o \
			Assembler.o \
			AssemblerDataBuilder.o \
			BinaryBuilder.o \
			Vector.o \
			ProgramArray.o \
			Table.o \
			Program.o
CC = gcc

# -Wno-missing-braces because of a known GCC bug, https://gcc.gnu.org/bugzilla/show_bug.cgi?id=25137
FLAGS = -ansi -pedantic -Wall -Werror -Wno-missing-braces

all: assembler

$(SRCS): %.o: %.c
	@echo Compiling $<
	@$(CC) $(FLAGS) -o $@ -c $<

assembler: $(SRCS)
	@echo Linking
	@$(CC) $(SRCS) -o $@

clean:
	@rm *.o
	@rm assembler