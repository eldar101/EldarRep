/*This is a program for formatting a fat12 file system*/
/*Eldar Weiss 303169783 maman 13, Operating systems 04.01.2019*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <math.h>
#include <time.h>
#include <linux/types.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>

#include "fat12.h"

#define _WORD(x) ((unsigned char)(x)[0] + (((unsigned char)(x)[1]) << 8))

int fid; /* global variable set by the open() function */
int fd_write(int sector_number, char *buffer);

int main(int argc, char *argv[]) {
	boot_record_t boot;

	if (argc != 2) {
		printf("Usage: %s <floppy_image>\n", argv[0]);
		exit(1);
	}

	/* Trying to open the file */
	if ((fid = open(argv[1], O_RDWR | O_CREAT, 0644)) >= 0) {
		boot.bootjmp[0] = 0xEB;
		boot.bootjmp[1] = 0x3C;
		boot.bootjmp[2] = 0x90;
		boot.sector_size = DEFAULT_SECTOR_SIZE;
		boot.sectors_per_cluster = 1;
		boot.reserved_sector_count = 1;
		boot.number_of_fats = 2;
		boot.number_of_dirents = 224;
		boot.sector_count = 2880;
		boot.media_type = 0xF0;
		boot.fat_size_sectors = 9;
		boot.sectors_per_track = 18;
		boot.nheads = 2;
		boot.sectors_hidden = 0;
		boot.sector_count_large = 0;
	} else {
		perror("Error: ");
		exit(1);
	}

	/* Step 1, write boot sector */
	char buf[DEFAULT_SECTOR_SIZE] = { 0 };
	memcpy(buf, &boot, sizeof(boot));
	fd_write(0, buf);

	/* Initiate first 2 entries to be reserved in FAT
	 * Each FAT contains (9 * 512) / 1.5 = 3072 entries */
	char fatReservedEnt[DEFAULT_SECTOR_SIZE] = { 0 };
	fatReservedEnt[0] = 0xF0;
	fatReservedEnt[1] = 0xFF;
	fatReservedEnt[2] = 0xF0;

	/* Step 2. Set FAT1/FAT2 table entires to 0x0000 (unused cluster)
	 * according to the fat12.pdf*/

	/* FAT1 first entry in first sector - reserved */
	fd_write(1, fatReservedEnt);

	/* Zeroing rest of FAT1	entries (unclustered) */
	char bufZero[DEFAULT_SECTOR_SIZE] = { 0 };
	for (int i = 2; i < 10; i++)
		fd_write(i, bufZero);

	/* FAT2 first entry in first sector - reserved */
	fd_write(10, fatReservedEnt);

	/* Zeroing rest of FAT2	entries (unclustered) */
	for (int i = 11; i < 19; i++)
		fd_write(i, bufZero);

	/* Step 3. Set direntries as free according to the fat12.pdf */
	for (int i = 19; i < 33; i++)
		fd_write(i, bufZero);

	/* Step 4. Handle data block
	 * Zeroing all data clusters */
	for (int i = 33; i < boot.sector_count; i++)
		fd_write(i, bufZero);

	if (close(fid) < 0) {
		perror("Error: ");
		exit(1);
	}

	/* Print boot record info */
	printf("sector_size: %d\n", boot.sector_size);
	printf("sectors_per_cluster: %d\n", boot.sectors_per_cluster);
	printf("reserved_sector_count: %d\n", boot.reserved_sector_count);
	printf("number_of_fats: %d\n", boot.number_of_fats);
	printf("number_of_dirents: %d\n", boot.number_of_dirents);
	printf("sector_count: %d\n", boot.sector_count);

	return 0;
}

int fd_write(int sector_number, char *buffer) {
	int dest, len;
	int bps = DEFAULT_SECTOR_SIZE;
	dest = lseek(fid, sector_number * DEFAULT_SECTOR_SIZE, SEEK_SET);
	if (dest != sector_number * bps) {
		/* Error handling */
		perror("Error: ");
		exit(1);
	}
	len = write(fid, buffer, bps);
	if (len != bps) {
		/* error handling*/
		perror("Error3: ");
		exit(1);
	}
	return len;
}

