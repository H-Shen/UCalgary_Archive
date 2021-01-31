#include <bits/stdc++.h>

using namespace std;

namespace Quick_Sort_Inplace {
    int partition(vector<int> &A, int l, int r) {
        int pivot = A.at(r);
        int i = l - 1;
        int j;
        for (j = l; j <= r - 1; j++) {
            if (A.at(j) <= pivot) {
                i++;
                swap(A[i], A[j]);
            }
        }
        swap(A.at(i + 1), A.at(r));
        return i + 1;
    }
    void quickSort(vector<int> &A, int l, int r) {
        if (l < r) {
            int m = partition(A, l, r);
            quickSort(A, l, m - 1);
            quickSort(A, m, r);
        }
    }
}

int main() {
    vector<int> A = {2,1};
    Quick_Sort_Inplace::quickSort(A, 0, (int)A.size() - 1);
    for (const auto &i : A) cout << i << '\n';
    return 0;
}
