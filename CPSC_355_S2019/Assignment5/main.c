#include <stdio.h>
#include <stdlib.h>

#define TRUE 1
#define FALSE 0

char *month[] = {"January", "February", "March", "April", "May",
                 "June", "July", "August", "September", "October",
                 "November", "December"};

char *season[] = {"Winter", "Spring", "Summer", "Fall"};

char error_message[] = "usage: a5b mm dd\n";

// range check for month
int month_check(int month_) {
    if (month_ < 1) {
        return FALSE;
    }
    if (month_ > 12) {
        return FALSE;
    }
    return TRUE;
}

// range check for day
int day_check(int month_, int day) {

    int months_list[] = {1, 3, 5, 7, 8, 10, 12};
    int months_length = sizeof(months_list) / sizeof(months_list[0]);
    int i = 0;
    for (i = 0; i < months_length; ++i) {
        if (months_list[i] == month_) {
            if (day < 1 || day > 31) {
                return FALSE;
            }
            return TRUE;
        }
    }

    if (month_ == 2) {
        if (day < 1 || day > 28) {
            return FALSE;
        }
        return TRUE;
    }

    if (day < 1 || day > 30) {
        return FALSE;
    }
    return TRUE;
}

int season_find(int month_, int day_) {
    // Winter
    if (month_ == 12 && day_ >= 21) {
        return 0;
    }
    if (month_ == 1 || month_ == 2) {
        return 0;
    }
    if (month_ == 3) {
        if (day_ <= 20) {
            return 0;
        }
        // Spring
        return 1;
    }
    if (month_ == 4 || month_ == 5) {
        return 1;
    }
    if (month_ == 6) {
        if (day_ <= 20) {
            return 1;
        }
        // Summer
        return 2;
    }
    if (month_ == 7 || month_ == 8) {
        return 2;
    }
    if (month_ == 9 && day_ <= 20) {
        return 2;
    }
    // Fall
    return 3;
}

int main(int argc, char ** argv) {

    if (argc != 3) {
        printf("%s", error_message);
        return 0;
    }

    int argv1 = atoi(argv[1]);

    // range check for month
    if (!month_check(argv1)) {
        printf("%s", error_message);
        return 0;
    }

    int argv2 = atoi(argv[2]);

    // range check for day
    if (!day_check(argv1, argv2)) {
        printf("%s", error_message);
        return 0;
    }

    int season_index = season_find(argv1, argv2);

    // adding suffix
    char *suffix = NULL;
    if (argv2 == 1 || argv2 == 21 || argv2 == 31) {
        suffix = "st";
    }
    else if (argv2 == 2 || argv2 == 22) {
        suffix = "nd";
    }
    else if (argv2 == 3) {
        suffix = "rd";
    }
    else {
        suffix = "th";
    }

    printf("%s %d%s is %s\n", month[argv1 - 1], argv2, suffix, season[season_index]);


    return 0;
}