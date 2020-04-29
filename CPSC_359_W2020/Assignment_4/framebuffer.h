// The size of the maze
#define WIDTH  16
#define HEIGHT 12

void initFrameBuffer();
void displayFrameBuffer();
void drawMaze(int rows, int columns, int square_size, int maze[HEIGHT][WIDTH], int is_player_shown, int is_game_end, int current_player_i, int current_player_j);
void displayFrameBufferForMaze(int maze[HEIGHT][WIDTH], int is_player_shown, int is_game_end, int current_player_i, int current_player_j);
void drawSquare(int rowStart, int columnStart, int squareSize, unsigned int color);
