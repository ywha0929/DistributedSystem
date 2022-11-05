#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>


#define MAXLINE 1500
#define NUMPERLINE 300

int createThreeDigitNumber()
{
	int result = 0;
	while(!(result > 99 && result < 1000))
	{
		result = rand()%1000;
	}
}

int main(int argc, char** argv)
{
	srand(time(NULL));
	int random = 0;
	char line[MAXLINE] = "";
	FILE* fd = fopen("./TestFile.txt","w+");
	int nLines = atoi(argv[2]);
	int nPerLine = atoi(argv[1]);
	//printf("how Many Lines : ");
	//scanf("%d",&nLines);
	//printf("check1\n");
	for(int i = 0; i<nLines; i++)
	{
		for(int j = 0; j< nPerLine; j++)
		{
			random = createThreeDigitNumber();
			char charNumber[10];
			sprintf(charNumber,"%d ",random);
			strcat(line,charNumber);
		}
		strcat(line,"\n");
		fputs(line,fd);
		strcpy(line,"");
	}
}
