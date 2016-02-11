
#include <"function1.h">

int function1(int a);

int main() {
	int a = 3;
	function1(a);
	cout << a;
}

int function1(int a){
	a = a + 1;
}