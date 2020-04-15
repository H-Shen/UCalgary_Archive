/**
 * An implementation of a binary search tree.
 * To compile in g++ -std=c++17 -O2
 */
#include <memory>
#include <iostream>
#include <queue>

namespace Binary_search_tree {

    // Define a node of a BST
    template<typename T>
    struct Node {
        T value;
        std::shared_ptr<Node> father;
        std::shared_ptr<Node> left_child;
        std::shared_ptr<Node> right_child;
        Node() = delete;
        explicit Node(const T &v) :
                value(v),
                father(nullptr),
                left_child(nullptr),
                right_child(nullptr) {}
    };

    // Return true if the BST includes a node whose value is given,
    // otherwise return false
    template<typename T>
    inline
    bool search(const std::shared_ptr<Node<T> > &root, const T &value) {
        // Case 1:
        if (!root) {
            return false;
        }
        // Case 2:
        else if (value < root->value) {
            return search(root->left_child, value);
        }
        // Case 3:
        else if (value > root->value) {
            return search(root->right_child, value);
        }
        // Case 4:
        return true;
    }

    template <typename T>
    inline
    void pre_order_traverse(const std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return;
        }
        std::cout << root->value << std::endl;
        pre_order_traverse(root->left_child);
        pre_order_traverse(root->right_child);
    }

    template <typename T>
    inline
    void in_order_traverse(const std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return;
        }
        in_order_traverse(root->left_child);
        std::cout << root->value << std::endl;
        in_order_traverse(root->right_child);
    }

    template <typename T>
    inline
    void post_order_traverse(const std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return;
        }
        post_order_traverse(root->left_child);
        post_order_traverse(root->right_child);
        std::cout << root->value << std::endl;
    }

    template <typename T>
    inline
    void level_order_traverse(const std::shared_ptr<Node<T> > &root) {
        std::queue<std::shared_ptr<Node<T> > >q;
        q.push(root);
        std::shared_ptr<Node<T> > node = nullptr;
        while (!q.empty()) {
            node = q.front();
            q.pop();
            if (node != nullptr) {
                std::cout << node->value << std::endl;
            }
            if (node->left_child) {
                q.push(node->left_child);
            }
            if (node->right_child) {
                q.push(node->right_child);
            }
        }
    }

    // Check if two trees are the same(same shape, same nodes)
    template <typename T>
    inline
    bool is_same_tree(const std::shared_ptr<Node<T> > &lhs, const std::shared_ptr<Node<T> > &rhs) {
        if (!lhs && !rhs) {
            return true;
        }
        if (!lhs || !rhs) {
            return false;
        }
        if (lhs->value == rhs->value) {
            return is_same_tree(lhs->left_child, rhs->left_child) &&
                   is_same_tree(lhs->right_child, rhs->right_child);
        }
        return false;
    }

    // Check if two trees have the same shape
    template <typename T>
    inline
    bool is_same_shape(const std::shared_ptr<Node<T> > &lhs, const std::shared_ptr<Node<T> > &rhs) {
        if (!lhs && !rhs) {
            return true;
        }
        if (!lhs || !rhs) {
            return false;
        }
        return is_same_shape(lhs->left_child, rhs->left_child) &&
               is_same_shape(lhs->right_child, rhs->right_child);
    }

    // Obtain the maximal depth of a BST
    template <typename T>
    inline
    int get_height(const std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return 0;
        }
        return 1 + std::max(get_height(root->left_child), get_height(root->right_child));
    }

    // Obtain a mirror of a BST
    template <typename T>
    inline
    void mirror(std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return;
        }
        std::swap(root->left_child, root->right_child);
        mirror(root->left_child);
        mirror(root->right_child);
    }

    // Insert a node to the BST
    template <typename T>
    inline
    std::shared_ptr<Node<T> > insert(std::shared_ptr<Node<T> > &root, const T &value) {
        if (!root) {
            root = std::make_shared<Node<T> >(value);
        }
        else if (value < root->value) {
            if (!root->left_child) {
                root->left_child = std::make_shared<Node<T> >(value);
                root->left_child->father = root;
            } else {
                root->left_child = insert(root->left_child, value);
            }
        }
        else if (value > root->value) {
            if (!root->right_child) {
                root->right_child = std::make_shared<Node<T> >(value);
                root->right_child->father = root;
            } else {
                root->right_child = insert(root->right_child, value);
            }
        } else {
            // No duplicates are allowed
            throw std::runtime_error("Value duplicated.");
        }
        return root;
    }
    //    vector<int> p = {1, 2, 6, 4, 7, -1, 1};
    //    auto const_iter = p.cbegin();
    //    shared_ptr<Binary_search_tree::Node<int> > BSTInstance = nullptr;
    //    while (const_iter != p.cend()) {
    //        BSTInstance = Binary_search_tree::insert(BSTInstance, *const_iter);
    //        ++const_iter;
    //    }
    //    Binary_search_tree::in_order_traverse(BSTInstance);

    template <typename T>
    inline
    bool empty(const std::shared_ptr<Node<T> > &root) {
        return !root;
    }
    //  std::shared_ptr<Binary_search_tree::Node<int> > BSTInstance = nullptr;
    //  std::cout << std::boolalpha << Binary_search_tree::empty(BSTInstance) << std::endl;

    // Helper function: get the successor of root(the node with the smallest value in the right subtree)
    // Pre-condition: root has a right subtree
    template <typename T>
    inline
    std::shared_ptr<Node<T> > get_successor(const std::shared_ptr<Node<T> > &root) {
        std::shared_ptr<Node<T> > node = root->right_child;
        while (node->left_child != nullptr) {
            node = node->left_child;
        }
        return node;
    }

    // Helper function: remove the node
    // Pre-condition: root is not null
    template <typename T>
    inline
    void delete_a_node(std::shared_ptr<Node<T> > &root) {
        if (root->left_child == nullptr) {
            // Case 1: left is null, right is null
            if (root->right_child == nullptr) {
                if (root->father == nullptr) {
                    root = nullptr;
                } else {
                    std::shared_ptr<Node<T> > father_node = root->father;
                    // When the root is the left child of its father
                    // we break the relationship between root and its father
                    if (father_node->value > root->value) {
                        father_node->left_child = nullptr;
                    }
                        // When the root is the right child of its father
                        // we break the relationship between root and its father
                    else {
                        father_node->right_child = nullptr;
                    }
                }
            }
                // Case 2: left is null, right is not null
            else {
                std::shared_ptr<Node<T> > right_child_node = root->right_child;
                // When root has no parents, we make its right child as new root
                if (root->father == nullptr) {
                    right_child_node->father = nullptr;
                    root = right_child_node;
                }
                else {
                    std::shared_ptr<Node<T> > father_node = root->father;
                    // When root is its father's left child
                    if (root->value < father_node->value) {
                        father_node->left_child = right_child_node;
                    }
                        // When root is its father's right child
                    else {
                        father_node->right_child = right_child_node;
                    }
                    // Update the father information of the right child
                    right_child_node->father = father_node;
                }
            }
        }
            // Case 3: left is not null, right is null
        else if (root->right_child == nullptr) {
            std::shared_ptr<Node<T> > left_child_node = root->left_child;
            // When root has no parents, we make its left child as new root
            if (root->father == nullptr) {
                left_child_node->father = nullptr;
                root = left_child_node;
            }
            else {
                std::shared_ptr<Node<T> > father_node = root->father;
                // When root is its father's left child
                if (root->value < father_node->value) {
                    father_node->left_child = left_child_node;
                }
                    // When root is its father's right child
                else {
                    father_node->right_child = left_child_node;
                }
                left_child_node->father = father_node;
            }
        }
            // Case 4: left is not null, right is not null
        else {
            std::shared_ptr<Node<T> > successor = get_successor(root);
            root->value = successor->value;
            delete_a_node(successor);
        }
    }

    // Delete a node whose value is key
    template <typename T>
    inline
    void erase(std::shared_ptr<Node<T> > &root, T key) {
        // Case 1:
        if (root == nullptr) {
            return;
        }
        // Case 2:
        if (key < root->value) {
            erase(root->left_child, key);
        }
            // Case 3:
        else if (key > root->value) {
            erase(root->right_child, key);
        }
            // Case 4:
        else {
            delete_a_node<T>(root);
        }
    }
    //    std::vector<int> A = {2, 4, 1, 7, 8, 3};
    //    std::shared_ptr<Binary_search_tree::Node<int> > Tree;
    //    for (const auto &i : A) {
    //        Tree = Binary_search_tree::insert(Tree, i);
    //    }
    //    Binary_search_tree::erase(Tree, 0);
    //    Binary_search_tree::in_order_traverse(Tree);

    // Get the number of nodes in the BST
    // An alternative way is storing the size as a data member, thus
    // we can obtain the number of nodes by root->size in O(1)
    template <typename T>
    inline
    int get_size(std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return 0;
        }
        return get_size(root->left_child) + get_size(root->right_child) + 1;
    }

    // Get the root of a given node in a BST
    template <typename T>
    inline
    std::shared_ptr<Node<T> > get_root(std::shared_ptr<Node<T> > node) {
        if (node) {
            while (node->father) {
                node = node->father;
            }
        }
        return node;
    }

    // Get the minimal value of nodes in a BST
    // the minimal value should be on the node that is a leaf on the left-most
    // chain
    // Return -1 if the given node is null
    template <typename T>
    inline
    T get_min(std::shared_ptr<Node<T> > node) {
        if (node) {
            while (node->left_child) {
                node = node->left_child;
            }
            return node->value;
        }
        return T(-1);
    }
    // Get the maximal value of nodes in a BST
    // the minimal value should be on the node that is a leaf on the right-most
    // chain
    // Return -1 if the given node is null
    template <typename T>
    inline
    T get_max(std::shared_ptr<Node<T> > node) {
        if (node) {
            while (node->right_child) {
                node = node->right_child;
            }
            return node->value;
        }
        return T(-1);
    }
}

int main() {

    std::vector<int> A = {2, 4, 7, 8, 3, 6, 1, 101001, -123};
    std::shared_ptr<Binary_search_tree::Node<int> > Tree;
    Binary_search_tree::post_order_traverse(Tree);
    for (const auto &i : A) {
        Tree = Binary_search_tree::insert(Tree, i);
    }
    Binary_search_tree::post_order_traverse(Tree);
    std::cout << Binary_search_tree::get_min(Tree) << std::endl;
    std::cout << Binary_search_tree::get_max(Tree) << std::endl;

    return 0;
}
