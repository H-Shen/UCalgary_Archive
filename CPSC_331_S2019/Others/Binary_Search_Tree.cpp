/**
 * An implementation of a binary search tree.
 * To compile in g++ -std=c++17
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
    bool search(const std::shared_ptr<Node<T> > &root, const T &value) {
        // Case 1:
        if (!root) {
            return false;
        }
        // Case 2:
        else if (value < root->left_child) {
            return search(root->left_child, value);
        }
        // Case 3:
        else if (value > root->right_child) {
            return search(root->right_child, value);
        }
        // Case 4:
        return true;
    }

    template <typename T>
    void pre_order_traverse(const std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return;
        }
        std::cout << root->value << std::endl;
        pre_order_traverse(root->left_child);
        pre_order_traverse(root->right_child);
    }

    template <typename T>
    void in_order_traverse(const std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return;
        }
        in_order_traverse(root->left_child);
        std::cout << root->value << std::endl;
        in_order_traverse(root->right_child);
    }

    template <typename T>
    void post_order_traverse(const std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return;
        }
        post_order_traverse(root->left_child);
        post_order_traverse(root->right_child);
        std::cout << root->value << std::endl;
    }

    template <typename T>
    void level_order(const std::shared_ptr<Node<T> > &root) {
        std::queue<std::shared_ptr<Node<T> > >q;
        q.push(root);
        std::shared_ptr<Node<T> > node = nullptr;
        while (!q.empty()) {
            node = q.front();
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

    // Check if two trees are the same shape
    template <typename T>
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
    int get_height(const std::shared_ptr<Node<T> > &root) {
        if (!root) {
            return 0;
        }
        return 1 + std::max(get_height(root->left_child), get_height(root->right_child));
    }

    // Obtain a mirror of a BST
    template <typename T>
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
    bool empty(const std::shared_ptr<Node<T> > &root) {
        return (root == nullptr);
    }
    //  std::shared_ptr<Binary_search_tree::Node<int> > BSTInstance = nullptr;
    //  std::cout << std::boolalpha << Binary_search_tree::empty(BSTInstance) << std::endl;
}

int main() {

    return 0;
}