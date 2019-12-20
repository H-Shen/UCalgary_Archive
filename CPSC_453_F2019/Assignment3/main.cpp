/**
 * This file includes all data structures, methods used to create a simple ray-tracer.
 *
 * 1. All data structures are written in C-style structs, only limited OOP stuff are used.
 *
 * 2. Includes a simple .tga parser in the struct 'Texture' constructor,
 *    it uses bi-linear filtering as well.
 *
 * 3. Includes a simple .obj loader in the struct 'TriangleMesh'
 *
 * 4. I referred some implementation of algorithms about 'Spatial Subdivisions',
 *    'Hierarchical Bounding Volumes' and 'Beer–Lambert law' from
 *    https://www.flipcode.com/archives/
 *    https://www.flipcode.com/archives/A%20faster%20voxel%20traversal%20algorithm%20for%20ray%20tracing.pdf,
 *    especially the grid traversal algorithm, honestly they are not easy
 *    to understand completely, so I followed some of their coding structure.
 *
 * 5. Textures were downloaded from www.turbosquid.com and they can used for
 *    education purpose under its free license.
 *
 * Created by: Haohu Shen
 * UCID: 30063099
 * Date: 2019-11-19
 */

#include <bits/stdc++.h>
#include <experimental/filesystem>

// #define __CLION

// Namespace: Math
// Description: The namespace includes all methods and data structures for 3D Geometry
// I used in this assignment.
namespace Math {

    // Three-way-compare the float number and the zero
    // result = 1: larger than 0
    // result = 0: equal to 0
    // result = -1: smaller than 0
    inline
    int threeWayComparator(const double &a) {
        constexpr double Eps = 1e-10;
        if (a > Eps) {
            return 1;
        }
        if (a > -Eps) {
            return 0;
        }
        return -1;
    }

    // Structure: Vec3
    //
    // Description: A 3D Vector that holds positional data using 3 doubles.
    struct Vec3 {

        // Fields
        double x;
        double y;
        double z;

        // Alternative getters for RGB
        [[nodiscard]] double r() const {
            return x;
        }

        [[nodiscard]] double g() const {
            return y;
        }

        [[nodiscard]] double b() const {
            return z;
        }

        // Constructor
        explicit Vec3(double x, double y, double z) : x(x), y(y), z(z) {}

        // Default Constructor
        Vec3() : Vec3(0.0, 0.0, 0.0) {}

        // Operator overload: [] access only
        double operator[](int index) const {
            // Check if index is valid
            assert(index >= 0 && index <= 2);
            switch (index) {
                case 0:
                    return x;
                case 1:
                    return y;
                default:
                    return z;
            }
        }

        // Operator overload: [] for assignment
        double &operator[](int index) {
            // Check if index is valid
            assert(index >= 0 && index <= 2);
            switch (index) {
                case 0:
                    return x;
                case 1:
                    return y;
                default:
                    return z;
            }
        }

        // Operator overload: ==
        bool operator==(const Vec3 &other) const {
            return (Math::threeWayComparator(x - other.x) == 0 &&
                    Math::threeWayComparator(y - other.y) == 0 &&
                    Math::threeWayComparator(z - other.z) == 0);
        }

        // Operator overload: !=
        bool operator!=(const Vec3 &other) const {
            return !(*this == other);
        }

        // Operator overload: +
        Vec3 operator+(const Vec3 &rhs) const {
            return Vec3(x + rhs.x, y + rhs.y, z + rhs.z);
        }

        // Operator overload: -
        Vec3 operator-(const Vec3 &rhs) const {
            return Vec3(x - rhs.x, y - rhs.y, z - rhs.z);
        }

        // Operator overload: /
        Vec3 operator/(const double &scalar) const {
            return Vec3(x / scalar, y / scalar, z / scalar);
        }

        // Operator overload: +=
        Vec3 &operator+=(const Vec3 &rhs) {
            x += rhs.x;
            y += rhs.y;
            z += rhs.z;
            return *this;
        }

        // Operator overload: -=
        Vec3 &operator-=(const Vec3 &rhs) {
            x -= rhs.x;
            y -= rhs.y;
            z -= rhs.z;
            return *this;
        }

        // Operator overload: *=
        Vec3 &operator*=(const double &rhs) {
            x *= rhs;
            y *= rhs;
            z *= rhs;
            return *this;
        }

        // Operator overload: /=
        Vec3 &operator/=(const double &rhs) {
            x /= rhs;
            y /= rhs;
            z /= rhs;
            return *this;
        }

        // Operator /= overload of two Vec3, it works component-wise like GLSL.
        Vec3 &operator/=(const Vec3 &rhs) {
            x /= rhs.x;
            y /= rhs.y;
            z /= rhs.z;
            return *this;
        }

        // Operator overload: -()
        Vec3 operator-() const {
            return Vec3(-x, -y, -z);
        }

        // Operator overload: +()
        const Vec3 &operator+() const {
            return *this;
        }
    };

    // Operator overload as non-member function: * (scalar)
    inline
    Vec3 operator*(const Vec3 &lhs, const double &scalar) {
        return Vec3(lhs.x * scalar, lhs.y * scalar, lhs.z * scalar);
    }

    inline
    Vec3 operator*(const double &scalar, const Vec3 &rhs) {
        return rhs * scalar;
    }

    // Operator * overload of two Vec3, it works component-wise like GLSL.
    inline
    Vec3 operator*(const Vec3 &lhs, const Vec3 &rhs) {
        return Vec3(
                lhs.x * rhs.x,
                lhs.y * rhs.y,
                lhs.z * rhs.z
        );
    }

    // Operator / overload of two Vec3, it works component-wise like GLSL.
    inline
    Vec3 operator/(const Vec3 &lhs, const Vec3 &rhs) {
        return Vec3(
                lhs.x / rhs.x,
                lhs.y / rhs.y,
                lhs.z / rhs.z
        );
    }

    // dot product
    inline
    double dot(const Vec3 &lhs, const Vec3 &rhs) {
        return (lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z);
    }

    // cross product
    inline
    Vec3 cross(const Vec3 &lhs, const Vec3 &rhs) {
        return Vec3(
                lhs.y * rhs.z - rhs.y * lhs.z,
                lhs.z * rhs.x - rhs.z * lhs.x,
                lhs.x * rhs.y - rhs.x * lhs.y
        );
    }

    // norm(length)
    inline
    double norm(const Vec3 &obj) {
        return sqrt(dot(obj, obj));
    }

    // normalize
    inline
    Vec3 normalize(const Vec3 &obj) {
        double objLength = norm(obj);
        return obj / objLength;
    }
}

// Namespace: Utility
// Description: The namespace includes all miscellaneous methods
// I used in this assignment.
namespace Utility {

    // Check if 'filename' is a regular file instead of a directory
    inline
    bool isRegularFile(const std::string &filename) {
        return std::experimental::filesystem::is_regular_file(filename);
    }

    // Check if 'filename' has .obj as its suffix
    inline
    bool isObjFile(const std::string &filename) {
        return std::experimental::filesystem::path(filename).extension() ==
               ".obj";
    }

    // Check if 'filename' has .tga as its suffix
    inline
    bool isTgaFile(const std::string &filename) {
        return std::experimental::filesystem::path(filename).extension() ==
               ".tga";
    }

    // Check if the user has the permission to read 'filename'.
    inline
    bool
    hasReadPermission(const std::experimental::filesystem::path &filename) {
        auto perm = std::experimental::filesystem::status(
                filename).permissions();
        return (perm & std::experimental::filesystem::perms::owner_read) !=
               std::experimental::filesystem::perms::none;
    }

    inline
    void
    clamp(int &n, const int &lowerBound, const int &upperBound) {
        n = std::min(std::max(n, lowerBound), upperBound);
    }

    // Convert RGB doubles to the corresponding integers between 0-255
    inline
    std::tuple<int, int, int>
    rgb2Int(const Math::Vec3 &color, double div = 1.0) {

        std::tuple<int, int, int> result;
        std::get<0>(result) = static_cast<int>(color.r() * 255.99 / div);
        std::get<1>(result) = static_cast<int>(color.g() * 255.99 / div);
        std::get<2>(result) = static_cast<int>(color.b() * 255.99 / div);

        // Clamp RGB to make sure they are integers between 0-255
        clamp(std::get<0>(result), 0, 255);
        clamp(std::get<1>(result), 0, 255);
        clamp(std::get<2>(result), 0, 255);
        return result;
    }

    // Return the integer part of a double
    inline
    double getIntegerPart(const double &n) {
        return n - floor(n);
    }

    // A helper function to split a string by using whitespaces with unknown length as delimiter
    inline
    std::vector<std::string> splitByWhitespaces(const std::string &s) {
        std::istringstream iss(s);
        return std::vector<std::string>{std::istream_iterator<std::string>(iss),
                                        std::istream_iterator<std::string>()};
    }

    // A function that returns a random double such that -1.0 <= x < 1.0
    static std::random_device dev;
    static std::mt19937 random_generator(dev());
    static std::uniform_real_distribution<double> distribution(-1.0f, 1.0f);

    inline
    double getRandomDouble() {
        return distribution(random_generator);
    }

    // A function that returns a pair of random doubles (x0, y0) such that
    // -1.0 <= x0 < 1.0
    // -1.0 <= y0 < 1.0
    // x0^2 + y0^2 < 1 (inside the unit circle)
    inline std::pair<double, double> getRandomDoublePair() {
        double x0, y0;
        do {
            x0 = Utility::getRandomDouble();
            y0 = Utility::getRandomDouble();
        } while (x0 * x0 + y0 * y0 >= 1.0);
        return {x0, y0};
    }

    // A function that returns a Vec3 'result' from an array of Vec3
    // where each component in 'result' is minimum
    template<std::size_t SIZE>
    Math::Vec3 getMinimalComponent(std::array<Math::Vec3, SIZE> a) {
        Math::Vec3 result;
        result.x = std::min_element(a.cbegin(), a.cend(),
                                    [](const Math::Vec3 &lhs,
                                       const Math::Vec3 &rhs) {
                                        return (lhs.x < rhs.x);
                                    })->x;
        result.y = std::min_element(a.cbegin(), a.cend(),
                                    [](const Math::Vec3 &lhs,
                                       const Math::Vec3 &rhs) {
                                        return (lhs.y < rhs.y);
                                    })->y;
        result.z = std::min_element(a.cbegin(), a.cend(),
                                    [](const Math::Vec3 &lhs,
                                       const Math::Vec3 &rhs) {
                                        return (lhs.z < rhs.z);
                                    })->z;
        return result;
    }

    // A function that returns a Vec3 'result' from an array of Vec3
    // where each component in 'result' is maximal
    template<std::size_t SIZE>
    Math::Vec3 getMaximalComponent(std::array<Math::Vec3, SIZE> a) {
        Math::Vec3 result;
        result.x = std::max_element(a.cbegin(), a.cend(),
                                    [](const Math::Vec3 &lhs,
                                       const Math::Vec3 &rhs) {
                                        return (lhs.x < rhs.x);
                                    })->x;
        result.y = std::max_element(a.cbegin(), a.cend(),
                                    [](const Math::Vec3 &lhs,
                                       const Math::Vec3 &rhs) {
                                        return (lhs.y < rhs.y);
                                    })->y;
        result.z = std::max_element(a.cbegin(), a.cend(),
                                    [](const Math::Vec3 &lhs,
                                       const Math::Vec3 &rhs) {
                                        return (lhs.z < rhs.z);
                                    })->z;
        return result;
    }
}

// Namespace: Raytracer
// Description: The namespace includes all methods and data structures for the
// raytracer.
namespace Raytracer {

    // Constants
    constexpr int DEFAULT_WIDTH = 800;  // Default screen width
    constexpr int DEFAULT_HEIGHT = 600; // Default screen height
    constexpr double EPSILON = 0.0001;
    constexpr double INFINITY_ = 1e10;
    constexpr int SAMPLES = 512;
    constexpr int MAXIMAL_DEPTH_TO_TRACE = 6;
    constexpr int GRID_SIZE = 256; // The size of a grid in spatial subdivisions
    constexpr size_t MAX_LIGHTS = 5; // The maximal number of lights allowed in the scene
    constexpr int SUPER_SAMPLING = 9; // 9 times of super-sampling
    constexpr int MAXIMAL_COMPONENT = 200; // The maximal component of each axis of the grid
    constexpr int MINIMAL_COMPONENT = -100; // The minimal component of each axis of the grid
    constexpr int MAXIMAL_COMPONENT_FOR_A_PLANE = 20000;    // The maximal component of a bounding box of a plane
    constexpr int MINIMAL_COMPONENT_FOR_A_PLANE = -10000;   // THe minimal component of a bounding box of a plane

    // Define 3 types of geometries
    enum class GEOMETRY_TYPE {
        SPHERE,
        PLANE,
        TRIANGLE,
    };

    // Define 3 relationships between a ray and a geometry/bounding box
    enum class HIT_STATUS : int {
        Hit = 1,     // The ray hits the geometry or the bounding box
        NoIntersection = 0,  // The ray misses the geometry or the bounding box
        Inside = -1,  // The ray is inside the geometry or the bounding box
    };

    // Forward declaration of all structs
    struct TriangleMesh;

    struct Ray;

    struct Camera;

    struct Texture;

    struct Material;

    struct Bounding_box;

    struct Geometry;

    struct Triangle;

    struct Plane;

    struct Sphere;

    struct TriangleMesh;

    struct Scene;

    struct RenderingEngine;

    // 'Ray' defines a ray by its origin and its direction using 2 vectors
    // Let A = origin, B = direction, then a point on the ray can be defined as
    // P(t) = A + B * t where t is a Real number.
    struct Ray {

        // Constructor
        explicit Ray(
                const Math::Vec3 &origin,
                const Math::Vec3 &direction) :
                origin(origin),
                direction(direction) {}

        // Members
        Math::Vec3 origin;
        Math::Vec3 direction;
    };

    // Define a positionable camera
    struct Camera {

        // No default constructor
        Camera() = delete;

        // Constructor
        explicit Camera(
                Math::Vec3 eye,
                Math::Vec3 target,
                Math::Vec3 upVector,
                double dist,
                double width,
                double height,
                double ratio,
                double radiusOfAperture,
                double distanceToFocalPlane) :
                eye(eye),
                target(target),
                upVector(upVector),
                distance(dist),
                width(width),
                height(height),
                ratio(ratio),
                radiusOfAperture(radiusOfAperture),
                distanceToFocalPlane(distanceToFocalPlane) {}

        // Get the direction of the ray of the pixel's coordinate on the screen.
        Math::Vec3 getDirection(double x, double y) {
            Math::Vec3 directionVector =
                    distance * Math::normalize(target - eye);
            rightAxis = Math::normalize(Math::cross(directionVector, upVector));
            upAxis = Math::normalize(Math::cross(rightAxis, directionVector));
            Math::Vec3 result = (x * ratio - 0.5 * width) * rightAxis +
                                (y * ratio - 0.5 * height) * upAxis +
                                directionVector;
            return result;
        }

        // Randomly select a ray inside the aperture
        Ray getRandomRay(double x, double y) {
            // Randomly select a pair of (x_, y_) inside the unit circle
            double x_, y_;
            std::tie(x_, y_) = Utility::getRandomDoublePair();
            x_ = x_ * radiusOfAperture;
            y_ = y_ * radiusOfAperture;

            // Calculate the normalized ray
            Math::Vec3 origin = x_ * rightAxis + y_ * upAxis + eye;
            Math::Vec3 direction = getDirection(x, y);
            Math::Vec3 positionOfFocus =
                    direction * distanceToFocalPlane / distance + eye;
            Math::Vec3 rayDirection = positionOfFocus - origin;
            rayDirection = Math::normalize(rayDirection);

            return Ray(origin, rayDirection);
        }

        // Fields
        Math::Vec3 eye;         // Position of the eye
        Math::Vec3 target;      // Position of the target
        Math::Vec3 upVector;    // The up vector of the camera that points upwards in the world space
        Math::Vec3 rightAxis;           // The unit vector of right axis of the camera
        Math::Vec3 upAxis;           // The unit vector of up axis of the camera

        double distance;        // Distance between the eye and the projection plane
        double width;           // Width of the projection plane
        double height;          // Height of the projection plane
        double ratio;         // The ratio of a pixel over the world coordinate(the length in the world that a pixel is mapping to)
        double radiusOfAperture;         // The radius of the aperture
        double distanceToFocalPlane;         // The distance between the eye and the focal plane
    };

    struct Texture {
        // No default constructor
        Texture() = delete;

        // Constructor that loads a texture file
        explicit Texture(const char *p_File) {

            // Check if p_File is a valid .tga file
            std::string textureFileName = p_File;
            if (!Utility::isRegularFile(textureFileName)) {
                fprintf(stderr, "%s is not a valid file, exit the program...\n",
                        p_File);
                exit(EXIT_SUCCESS);
            }
            if (!Utility::isTgaFile(textureFileName)) {
                fprintf(stderr,
                        "%s is not a valid tga file, exit the program...\n",
                        p_File);
                exit(EXIT_SUCCESS);
            }

            // Try to open the TGA file.
            //FILE *fp = fopen(p_File, "rb");
            if (auto file_pointer = fopen(p_File, "rb")) {

                // Skip the first 20 bytes since we don't need it
                unsigned char bufferToSkip[20];
                fread(bufferToSkip, 1, 20, file_pointer);

                // Read the texture's width and height
                width = bufferToSkip[12] + (bufferToSkip[13] << 8);
                height = bufferToSkip[14] + (bufferToSkip[15] << 8);
                fclose(file_pointer);

                // Reset file_pointer and read data of each pixel
                file_pointer = fopen(p_File, "rb");

                // Set a pointer point to the head of color data in p_File
                // The reason why I add extra 100 here is to make sure
                // width * height * 3 + 100 > (width * height - 1) * 3 + 20
                // No need to calculate the offset accurately here since
                // we have enough space in the heap to allocate
                auto tempPointer = new unsigned char[width * height * 3 + 100];
                fread(tempPointer, 1, width * height * 3 + 100,
                      file_pointer);
                fclose(file_pointer);

                // Convert each 24-bit RGB to the corresponding value between 0.0 - 1.0
                bitmap = new Math::Vec3[width * height];
                int size = width * height;
                int offset = 20;
                for (int i = 0; i < size; ++i) {
                    bitmap[i] = Math::Vec3(
                            static_cast<double>(tempPointer[i * 3 + offset]) /
                            255.0,
                            static_cast<double>(tempPointer[i * 3 + offset -
                                                            1]) /
                            255.0,
                            static_cast<double>(tempPointer[i * 3 + offset -
                                                            2]) /
                            255.0);
                }
                // Free the space in the heap
                delete[] tempPointer;
            } else {
                fprintf(stderr, "Failed to load %s, exit the program...\n",
                        p_File);
                exit(EXIT_SUCCESS);
            }
        }

        // Destructor
        ~Texture() {
            // Free the space in the heap
            delete[] bitmap;
        }

        // Obtain the color vector (texture element) of the corresponding UV Coordinates
        Math::Vec3 getUVColor(const double &u, const double &v) {

            // Do texture filtering using bilinear interpolation
            auto u_ = (u + 2048.0 * 100.0) * width;
            auto v_ = (v + 2048.0 * 100.0) * height;

            auto u1 = static_cast<int> (u_) % width;
            auto u2 = (u1 + 1) % width;
            auto v1 = static_cast<int> (v_) % height;
            auto v2 = (v1 + 1) % height;

            // Obtain the integer parts of u_ and v_
            auto x = Utility::getIntegerPart(u_);
            auto y = Utility::getIntegerPart(v_);

            // Obtain 4 weight factors
            std::vector weightFactors{
                    (1.0 - x) * (1.0 - y),
                    (1.0 - x) * y,
                    x * (1.0 - y),
                    x * y
            };

            // Obtain the 4 texture elements
            std::vector elements{
                    bitmap[v1 * width + u1],
                    bitmap[v1 * width + u2],
                    bitmap[v2 * width + u1],
                    bitmap[v2 * width + u2]
            };
            // Do the interpolation by multiplication and summation
            std::vector<Math::Vec3> result;
            std::transform(weightFactors.begin(), weightFactors.end(),
                           elements.begin(),
                           std::back_inserter(result),
                           [](const auto &lhs, const auto &rhs) {
                               return lhs * rhs;
                           });
            return std::accumulate(result.begin(), result.end(), Math::Vec3());
        }

        Math::Vec3 *bitmap;
        int width;
        int height;
    };

    struct Material {

        // Default constructor
        Material() :
                reflection(0),
                diffuseReflection(0.0),
                diffuse(0.2),
                specular(0.8),
                refraction(0.0),
                refractiveIndex(1.5) {

        }

        // Color setter
        // A color and a texture cannot co-exist in a material
        // Thus when a new color is assigned, any previous texture will be
        // removed and the reciprocal of texture ratio will be reset
        void setColor(const Math::Vec3 &p_Color) {
            color = p_Color;
            texture_ptr = nullptr;
            textureScaleFactor = 1.0;
        }

        // Fields
        Math::Vec3 color;     // The color of the material
        double reflection;          // Reflection coefficient
        double diffuseReflection;   // diffuse reflection of dielectrics
        double diffuse;             // diffuse reflection of non-dielectrics
        double specular;
        double refraction;
        double refractiveIndex;
        std::shared_ptr<Texture> texture_ptr;         // Pointer to its texture
        double textureScaleFactor = 1.0;   // The texture scale factor
    };

    // Definition of Axis-aligned Bounding Box
    struct Bounding_box {

        // Default constructor
        Bounding_box() = default;

        // Constructor
        Bounding_box(const Math::Vec3 &p_Pos, const Math::Vec3 &p_Size)
                : minimum(
                p_Pos),
                  size(p_Size) {};

        // Check if two AABBs intersects
        bool hit(Bounding_box &rhs) {

            auto rhs_minimum = rhs.minimum;
            auto rhs_maximum = rhs.minimum + rhs.size;
            auto maximum = minimum + size;

            // Check if two boxes overlap in x-axis
            if (maximum.x > rhs_minimum.x && minimum.x < rhs_maximum.x) {
                // Check if two boxes overlap in y-axis
                if (maximum.y > rhs_minimum.y && minimum.y < rhs_maximum.y) {
                    // Check if two boxes overlap in z-axis
                    if (maximum.z > rhs_minimum.z &&
                        minimum.z < rhs_maximum.z) {
                        return true;
                    }
                }
            }
            return false;
        }

        // Fields
        Math::Vec3 minimum;   // The vector where each component is minimum in the corresponding
        // axis of the bounding box (x_min, y_min, z_min)
        Math::Vec3 size;  // The three sizes of the bounding box
    };

    // Define the geometry in the scene as an abstract class
    struct Geometry {

        Geometry() : isLight(false) {};

        // Virtual destructor
        virtual ~Geometry() = default;

        //Material *getMaterial() { return &material; }

        // Interface to get the type of the geometry;
        virtual GEOMETRY_TYPE type() = 0;

        // Interface that checks if the ray intersects the object
        // pass the value of 't' (t from p(t) = A + Bt) to p_Dist
        // if there is a hit
        virtual HIT_STATUS hit(Ray &p_Ray, double &p_Dist) = 0;

        // Interface that returns the Bounding_box of the geometry
        virtual Bounding_box getBoundingBox() = 0;

        // Interface that checks if the ray intersects the Bounding_box
        // of the object.
        virtual bool hitBoundingBox(Bounding_box &) = 0;

        // Interface that returns the normal vector
        virtual Math::Vec3 getNormal(Math::Vec3 &p_Pos) = 0;

        // Interface that returns the color of the geometry's material
        virtual Math::Vec3
        getColor(Math::Vec3 &p_Pos) { return material.color; }

        // Interface of the light setter
        virtual void Light(bool p_Light) { isLight = p_Light; }

        // Member
        Material material;  // A pointer points to its material
        bool isLight;       // Indicates if the geometry is a light
    };

    // Define the 'Triangle' as a derived class of 'Geometry'
    struct Triangle : public Geometry {

        // No default constructor
        Triangle() = delete;

        // Constructor that takes 3 vertices of the triangle
        // Store in counter-clockwise
        Triangle(Math::Vec3 A, Math::Vec3 B, Math::Vec3 C) {
            vertices.at(0) = A;
            vertices.at(1) = B;
            vertices.at(2) = C;
            normal = Math::normalize(Math::cross(B - A, C - B));
        }

        // Type getter
        GEOMETRY_TYPE type() override { return GEOMETRY_TYPE::TRIANGLE; }

        // Check if the ray hits the triangle
        HIT_STATUS hit(Ray &p_Ray, double &p_Dist) override {

            Math::Vec3 direction = p_Ray.direction;
            Math::Vec3 origin = p_Ray.origin;
            auto distance = -Math::dot((origin - vertices.at(0)), normal) /
                            Math::dot(direction, normal);

            // Case 1
            if ((distance < EPSILON) || (distance > INFINITY_)) {
                return HIT_STATUS::NoIntersection;
            }

            Math::Vec3 intersectedPoint = origin + distance * direction;
            double u;
            double v;

            Math::Vec3 dir_u = Math::normalize(vertices.at(1) - vertices.at(0));
            Math::Vec3 Hu1 =
                    vertices.at(0) +
                    dir_u * Math::dot(dir_u, (vertices.at(2) - vertices.at(0)));

            Math::Vec3 Hu2 =
                    vertices.at(0) +
                    dir_u *
                    Math::dot(dir_u, (intersectedPoint - vertices.at(0)));

            u = (intersectedPoint - Hu2).x / (vertices.at(2) - Hu1).x;

            // Case 2
            if (u < -EPSILON) {
                return HIT_STATUS::NoIntersection;
            }

            Math::Vec3 dir_v = Math::normalize(vertices.at(2) - vertices.at(0));

            Math::Vec3 Hv1 =
                    vertices.at(0) +
                    dir_v * Math::dot(dir_v, (vertices.at(1) - vertices.at(0)));

            Math::Vec3 Hv2 =
                    vertices.at(0) +
                    dir_v *
                    Math::dot(dir_v, (intersectedPoint - vertices.at(0)));

            v = (intersectedPoint - Hv2).x / (vertices[1] - Hv1).x;

            // Case 3
            if (v < -EPSILON) {
                return HIT_STATUS::NoIntersection;
            }

            // Case 4
            if (u + v > 1.0 + EPSILON) {
                return HIT_STATUS::NoIntersection;
            }

            // Case 5
            if (p_Dist > distance) {
                p_Dist = distance;
            } else {
                return HIT_STATUS::NoIntersection;
            }

            // Case 6
            return HIT_STATUS::Hit;
        }

        bool hitBoundingBox(Bounding_box &box) override {
            Bounding_box a = getBoundingBox();
            return a.hit(box);
        }

        // Interface that returns the normal of the triangle,
        // used in the Triangle Mesh
        virtual Math::Vec3 getNormal() { return normal; }

        Math::Vec3 getNormal(Math::Vec3 &p_Pos) override { return normal; }

        Bounding_box getBoundingBox() override {
            Math::Vec3 minimum = Utility::getMinimalComponent(vertices);
            Math::Vec3 maximum = Utility::getMaximalComponent(vertices);
            return Bounding_box(minimum, maximum - minimum);
        }

        // Member
        std::array<Math::Vec3, 3> vertices;
        Math::Vec3 normal;
    };

    // Define the 'Sphere' as a derived class of 'Geometry'
    struct Sphere : public Geometry {

        // Type getter
        GEOMETRY_TYPE type() override { return GEOMETRY_TYPE::SPHERE; }

        // No default constructor;
        Sphere() = delete;

        // Constructor
        Sphere(const Math::Vec3 &center, double radius) :
                center(center),
                radius(radius) {};

        Math::Vec3 &getCenterReference() { return center; }

        Math::Vec3 getColor(Math::Vec3 &p_Pos) override {
            // If the Sphere does not have a texture, then return its color.
            if (!material.texture_ptr) {
                return material.color;
            }
            // Otherwise return the texel with the position given
            Math::Vec3 r_Pos = p_Pos - center;
            double s = acos(r_Pos.z / radius) / M_PI;
            double t = acos(r_Pos.x / (radius * sin(s * M_PI))) / M_PI;
            return material.texture_ptr->getUVColor(s, t);
        }

        HIT_STATUS hit(Ray &p_Ray, double &p_Dist) override {
            Math::Vec3 v = p_Ray.origin - center;
            double b = -Math::dot(v, p_Ray.direction);
            double determinant = (b * b) - Math::dot(v, v) + radius * radius;

            HIT_STATUS retval = HIT_STATUS::NoIntersection;
            if (determinant > EPSILON) {
                determinant = sqrt(determinant);
                double i1 = b - determinant;
                double i2 = b + determinant;
                if (i2 > EPSILON) {
                    if (i1 < EPSILON) {
                        if (i2 < p_Dist) {
                            p_Dist = i2;
                            retval = HIT_STATUS::Inside;
                        }
                    } else {
                        if (i1 < p_Dist) {
                            p_Dist = i1;
                            retval = HIT_STATUS::Hit;
                        }
                    }
                }
            }
            return retval;
        }

        bool hitBoundingBox(Bounding_box &p_b) override {
            double d_min = 0;
            Math::Vec3 v1 = p_b.minimum;
            Math::Vec3 v2 = v1 + p_b.size;
            if (center.x < v1.x) {
                d_min += (center.x - v1.x) * (center.x - v1.x);
            } else if (center.x > v2.x) {
                d_min += (center.x - v2.x) * (center.x - v2.x);
            }
            if (center.y < v1.y) {
                d_min += (center.y - v1.y) * (center.y - v1.y);
            } else if (center.y > v2.y) {
                d_min += (center.y - v2.y) * (center.y - v2.y);
            }
            if (center.z < v1.z) {
                d_min += (center.z - v1.z) * (center.z - v1.z);
            } else if (center.z > v2.z) {
                d_min += (center.z - v2.z) * (center.z - v2.z);
            }
            return (d_min <= radius * radius);
        }

        // Obtain the unit normal vector of the 'position'
        Math::Vec3 getNormal(Math::Vec3 &position) override {
            return (position - center) / radius;
        }

        Bounding_box getBoundingBox() override {
            Math::Vec3 offset(radius, radius, radius);
            return Bounding_box(center - offset, 2 * offset);
        }

        // Fields
        Math::Vec3 center;
        double radius;
    };

    struct Plane : public Geometry {

        // Type getter
        GEOMETRY_TYPE type() override { return GEOMETRY_TYPE::PLANE; }

        // No default constructor
        Plane() = delete;

        // Constructor
        Plane(const Math::Vec3 &p_Normal, const double offset) :
                normal(p_Normal), offset(offset) {
            xAxis = Math::cross(normal, Math::Vec3(0, 1, 0));
            yAxis = Math::cross(xAxis, normal);
            xAxis = Math::normalize(xAxis);
            yAxis = Math::normalize(yAxis);
        }

        Math::Vec3 getColor(Math::Vec3 &position) override {
            // If the plane does not have a texture, return its color
            if (!material.texture_ptr) {
                return material.color;
            }
            double u = Math::dot(position, xAxis) / material.textureScaleFactor;
            double v = Math::dot(position, yAxis) / material.textureScaleFactor;
            return material.texture_ptr->getUVColor(u, v);
        }

        HIT_STATUS hit(Ray &p_Ray, double &p_Dist) override {
            double d = Math::dot(normal, p_Ray.direction);
            if (d != 0) {
                double dist =
                        -(Math::dot(normal, p_Ray.origin) + offset) / d;
                if (dist > 0) {
                    if (dist < p_Dist) {
                        p_Dist = dist;
                        return HIT_STATUS::Hit;
                    }
                }
            }
            return HIT_STATUS::NoIntersection;
        }

        bool hitBoundingBox(Bounding_box &p_b) override {

            Math::Vec3 pos = p_b.minimum;
            Math::Vec3 size = p_b.size;
            int num1 = 0;
            int num2 = 0;

            for (int i = 0; i < 2; ++i)
                for (int j = 0; j < 2; ++j)
                    for (int k = 0; k < 2; ++k) {
                        Math::Vec3 newPosition =
                                pos +
                                Math::Vec3(i * size.x, j * size.y, k * size.z);
                        if ((Math::dot(newPosition, getNormalReference()) +
                             offset) < 0) {
                            ++num1;
                        } else {
                            ++num2;
                        }
                    }
            return !(num1 == 0 || num2 == 0);
        }

        Math::Vec3 &getNormalReference() { return normal; }

        Math::Vec3 getNormal(Math::Vec3 &p_Pos) override { return normal; };

        Bounding_box getBoundingBox() override {
            // Make the bounding box of the plane large enough
            return Bounding_box(Math::Vec3(MINIMAL_COMPONENT_FOR_A_PLANE * 1.0,
                                           MINIMAL_COMPONENT_FOR_A_PLANE * 1.0,
                                           MINIMAL_COMPONENT_FOR_A_PLANE * 1.0),
                                Math::Vec3(MAXIMAL_COMPONENT_FOR_A_PLANE * 1.0,
                                           MAXIMAL_COMPONENT_FOR_A_PLANE * 1.0,
                                           MAXIMAL_COMPONENT_FOR_A_PLANE *
                                           1.0));
        }

        // Fields
        Math::Vec3 normal;
        double offset;      // The offset of the plane and the bounding box
        Math::Vec3 xAxis;   // The normalized vector of x-axis of the plane(in its local coordinates)
        Math::Vec3 yAxis;   // The normalized vector of y-axis of the plane(in its local coordinates)
    };

    // Define the 'Triangle Mesh' which is a collection of 'Triangle' instances
    struct TriangleMesh {

        // No default constructor
        TriangleMesh() = delete;

        // Constructor
        TriangleMesh(const Math::Vec3 &offset, double scale) :
                offset(offset),
                scale(scale) {}

        // A simple obj loader, supported 'v' and 'f' only without interpolated normal vectors
        void objLoader(const char *filename, Geometry **geometryList,
                       int &geometryNumber) {

            std::string filenameStr = filename;
            // Check if the file exists and make sure it is not a directory
            if (!Utility::isRegularFile(filenameStr)) {
                fprintf(stderr, "%s is not a valid file!\n", filename);
                exit(EXIT_SUCCESS);
            }
            // Check if the file ends with '.obj'
            if (!Utility::isObjFile(filenameStr)) {
                fprintf(stderr, "%s is not an .obj file!\n", filename);
                exit(EXIT_SUCCESS);
            }
            // Check if the user has the permission to read the file
            if (!Utility::hasReadPermission(
                    (const std::experimental::filesystem::path &) filenameStr)) {
                fprintf(stderr, "%s has no permission to read.\n", filename);
                exit(EXIT_SUCCESS);
            }

            std::ifstream ifs(filename);
            if (ifs.good()) {

                // Parse all lines
                std::vector<Math::Vec3> vertices;
                std::string tempStr;
                std::vector<std::string> currentLine;

                // We only handle lines start with 'v ', 'f'
                while (std::getline(ifs, tempStr)) {
                    currentLine = Utility::splitByWhitespaces(tempStr);
                    if (!currentLine.empty()) {

                        if (currentLine.front() == "v") {
                            double temp_x = std::stod(currentLine.at(1));
                            double temp_y = std::stod(currentLine.at(2));
                            double temp_z = std::stod(currentLine.at(3));
                            vertices.emplace_back(Math::Vec3(
                                    temp_x,
                                    temp_y,
                                    temp_z) * scale + offset);
                        } else if (currentLine.front() == "f") {
                            int temp_A = std::stoi(currentLine.at(1)) - 1;
                            int temp_B = std::stoi(currentLine.at(2)) - 1;
                            int temp_C = std::stoi(currentLine.at(3)) - 1;

                            auto tri = new Triangle(vertices.at(
                                    static_cast<unsigned long>(temp_A)),
                                                    vertices.at(
                                                            static_cast<unsigned long>(temp_B)),
                                                    vertices.at(
                                                            static_cast<unsigned long>(temp_C)));

                            if (Math::norm(tri->getNormal()) >= EPSILON) {
                                TriangleList.emplace_back(tri);
                                geometryList[geometryNumber] = tri;
                                // Update the number of geometries in the scene
                                ++geometryNumber;
                            }
                        }
                    }
                }
            } else {
                fprintf(stderr, "Failed to parse the .obj file.\n");
                exit(EXIT_SUCCESS);
            }
            //geometryNumber--;
        }

        // Material setter
        void setMaterial(Material *material_ptr) {
            for (auto &i : TriangleList) {
                i->material.setColor(material_ptr->color);
                i->material.diffuseReflection = material_ptr->diffuseReflection;
                i->material.diffuse = material_ptr->diffuse;
                i->material.reflection = material_ptr->reflection;
                i->material.refraction = material_ptr->refraction;
                i->material.refractiveIndex = material_ptr->refractiveIndex;
                i->material.specular = material_ptr->specular;
            }
        }

        // Fields
        Math::Vec3 offset;
        double scale;
        std::vector<Triangle *> TriangleList;
    };

    struct Scene {

        Scene() : geometryList(nullptr), lightList(nullptr), grid(nullptr) {
            numberOfGeometries = 0;
            numberOfLights = 0;
            lengthOfGeometryList = 0;
        };

        ~Scene() {
            if (lengthOfGeometryList != 0) {
                for (int i = 0; i < lengthOfGeometryList; ++i) {
                    delete geometryList[i];
                }
            }
            delete[] geometryList;

            if (lightList) {
                for (size_t i = 0; i != MAX_LIGHTS; ++i) {
                    delete lightList[i];
                }
            }
            delete[] lightList;

            if (grid) {
                for (auto &i : *grid) {
                    delete i;
                }
                delete grid;
            }

        }

        void drawScene(int sceneNumber) {

            switch (sceneNumber) {
                case 1: {

                    lengthOfGeometryList = 50;
                    geometryList = new Geometry *[lengthOfGeometryList];

                    // Define the ground plane
                    geometryList[0] = new Plane(Math::Vec3(0, 1, 0), 4.4f);
                    geometryList[0]->material.reflection = 0.6;
                    geometryList[0]->material.specular = 0.8;
                    geometryList[0]->material.refraction = 0.0;
                    geometryList[0]->material.diffuse = 1.0;
                    geometryList[0]->material.setColor(
                            Math::Vec3(0.3f, 0.3f, 0.3f));

                    // Define the back plane
                    geometryList[1] = new Plane(Math::Vec3(0, 0, -1), 12.4f);
                    geometryList[1]->material.specular = 0.4;
                    geometryList[1]->material.reflection = 0.0;
                    geometryList[1]->material.refraction = 0.0;
                    geometryList[1]->material.diffuse = 1.0;
                    geometryList[1]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 0.7f));

                    // small sphere
                    geometryList[2] = new Sphere(Math::Vec3(-5, -2.7f, 9), 1.5);
                    geometryList[2]->material.reflection = 0.8;
                    geometryList[2]->material.refraction = 0.0;
                    geometryList[2]->material.refractiveIndex = 1.3;
                    geometryList[2]->material.diffuse = 0.1;
                    geometryList[2]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));
                    geometryList[2]->material.diffuseReflection = 0.1;

                    // third sphere
                    geometryList[3] = new Sphere(Math::Vec3(4, -3.2f, 7), 1);
                    geometryList[3]->material.reflection = 0.1;
                    geometryList[3]->material.refraction = 0.9;
                    geometryList[3]->material.refractiveIndex = 1.5;
                    geometryList[3]->material.diffuse = 0.1;
                    geometryList[3]->material.specular = 0.2;
                    geometryList[3]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));
                    geometryList[3]->material.diffuseReflection = 0.1;

                    // Define the light source
                    geometryList[4] = new Sphere(Math::Vec3(0, 4.8, 5), 0.1f);
                    geometryList[4]->Light(true);
                    geometryList[4]->material.setColor(Math::Vec3(1, 1, 1));

                    // middle sphere2
                    geometryList[5] = new Sphere(Math::Vec3(4, -3.0f, 4), 1);
                    geometryList[5]->material.reflection = 0.0;
                    geometryList[5]->material.refraction = 0.0;
                    geometryList[5]->material.refractiveIndex = 1.3;
                    geometryList[5]->material.diffuse = 0.9;
                    geometryList[5]->material.specular = 0.2;
                    geometryList[5]->material.setColor(
                            Math::Vec3(0.7f, 0.3f, 1.0f));

                    // ceiling
                    geometryList[6] = new Plane(Math::Vec3(0, -1, 0), 5.2f);
                    geometryList[6]->material.specular = 0.8;
                    geometryList[6]->material.reflection = 0.0;
                    geometryList[6]->material.refraction = 0.0;
                    geometryList[6]->material.diffuse = 1.0;
                    geometryList[6]->material.setColor(
                            Math::Vec3(0.4f, 0.3f, 0.3f));

                    // right Plane
                    geometryList[7] = new Plane(Math::Vec3(1, 0, 0), 7.0f);
                    geometryList[7]->material.specular = 0.0;
                    geometryList[7]->material.reflection = 0.0;
                    geometryList[7]->material.refraction = 0.0;
                    geometryList[7]->material.diffuse = 0.7;
                    geometryList[7]->material.setColor(
                            Math::Vec3(0.12f, 0.45f, 0.15f));

                    // left Plane
                    geometryList[8] = new Plane(Math::Vec3(-1, 0, 0), 7.0f);
                    geometryList[8]->material.specular = 0.0;
                    geometryList[8]->material.reflection = 0.0;
                    geometryList[8]->material.refraction = 0.0;
                    geometryList[8]->material.diffuse = 0.8;
                    geometryList[8]->material.setColor(
                            Math::Vec3(0.65f, 0.05f, 0.05f));

                    // small sphere
                    geometryList[9] = new Sphere(Math::Vec3(-5.8, -3.0f, 5),
                                                 1.2);
                    geometryList[9]->material.reflection = 0.0;
                    geometryList[9]->material.refraction = 0.0;
                    geometryList[9]->material.refractiveIndex = 1.3;
                    geometryList[9]->material.diffuse = 0.9;
                    geometryList[9]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));

                    numberOfGeometries = 10;

                    TriangleMesh Tetrahedron(Math::Vec3(0, -3.0, 8.0), 5.0);

#ifdef __CLION
                    Tetrahedron.objLoader(
                            "/Users/hshen/CLionProject/Assignment3/Samples/Tetrahedron.obj",
                            geometryList, numberOfGeometries);
#else
                    Tetrahedron.objLoader(
                        "./Samples/Tetrahedron.obj",
                        geometryList, numberOfGeometries);
#endif
                    auto tetrahedron_material_ptr = new Material;
                    tetrahedron_material_ptr->setColor(
                            Math::Vec3(0.48, 0.46, 0.42));
                    tetrahedron_material_ptr->diffuseReflection = 0.0;
                    tetrahedron_material_ptr->diffuse = 2.0;
                    tetrahedron_material_ptr->reflection = 0.0;
                    tetrahedron_material_ptr->refraction = 0.0;
                    tetrahedron_material_ptr->refractiveIndex = 1.5;
                    tetrahedron_material_ptr->specular = 0.0;
                    Tetrahedron.setMaterial(tetrahedron_material_ptr);

                    break;
                }
                case 2: {

                    lengthOfGeometryList = 50;
                    geometryList = new Geometry *[lengthOfGeometryList];

                    // ground Plane
                    geometryList[0] = new Plane(Math::Vec3(0, 1, 0), 4.4f);
                    geometryList[0]->material.reflection = 0.6;
                    geometryList[0]->material.specular = 0.8;
                    geometryList[0]->material.refraction = 0.0;
                    geometryList[0]->material.diffuse = 1.0;
                    geometryList[0]->material.setColor(
                            Math::Vec3(0.3f, 0.3f, 0.3f));

                    // back Plane
                    geometryList[1] = new Plane(Math::Vec3(0, 0, -1), 12.4f);
                    geometryList[1]->material.specular = 0.4;
                    geometryList[1]->material.reflection = 0.0;
                    geometryList[1]->material.refraction = 0.0;
                    geometryList[1]->material.diffuse = 1.0;
#ifdef __CLION
                    geometryList[1]->material.texture_ptr = std::make_shared<Texture>(
                            "/Users/hshen/CLionProject/Assignment3/Texture/bricks_red.tga");
#else
                    geometryList[1]->material.texture_ptr = std::make_shared<Texture>(
                        "./Texture/bricks_red.tga");
#endif
                    geometryList[1]->material.textureScaleFactor = 10.0;

                    // small sphere
                    geometryList[2] = new Sphere(Math::Vec3(-5, -2.7f, 9), 1.5);
                    geometryList[2]->material.reflection = 0.8;
                    geometryList[2]->material.refraction = 0.0;
                    geometryList[2]->material.refractiveIndex = 1.3;
                    geometryList[2]->material.diffuse = 0.1;
                    geometryList[2]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));
                    geometryList[2]->material.diffuseReflection = 0.1;

                    // third sphere
                    geometryList[3] = new Sphere(Math::Vec3(4, -3.2f, 7), 1);
                    geometryList[3]->material.reflection = 0.1;
                    geometryList[3]->material.refraction = 0.9;
                    geometryList[3]->material.refractiveIndex = 1.5;
                    geometryList[3]->material.diffuse = 0.1;
                    geometryList[3]->material.specular = 0.2;
                    geometryList[3]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));
                    geometryList[3]->material.diffuseReflection = 0.1;

                    // light source 1
                    geometryList[4] = new Sphere(Math::Vec3(0, 4.8, 5), 0.1f);
                    geometryList[4]->Light(true);
                    geometryList[4]->material.setColor(Math::Vec3(1, 1, 1));

                    // big middle sphere
                    geometryList[5] = new Sphere(Math::Vec3(0, -1.7f, 7), 2.0);
                    geometryList[5]->material.reflection = 0.1;
                    geometryList[5]->material.refraction = 0.9;
                    geometryList[5]->material.refractiveIndex = 1.3;
                    geometryList[5]->material.diffuse = 0.1;
                    geometryList[5]->material.setColor(
                            Math::Vec3(1.0f, 1.0f, 1.0f));
                    geometryList[5]->material.diffuseReflection = 0.1;

                    // ceiling
                    geometryList[6] = new Plane(Math::Vec3(0, -1, 0), 5.2f);
                    geometryList[6]->material.specular = 0.8;
                    geometryList[6]->material.reflection = 0.0;
                    geometryList[6]->material.refraction = 0.0;
                    geometryList[6]->material.diffuse = 1.0;
                    geometryList[6]->material.setColor(
                            Math::Vec3(0.4f, 0.3f, 0.3f));

                    // right Plane
                    geometryList[7] = new Plane(Math::Vec3(1, 0, 0), 7.0f);
                    geometryList[7]->material.specular = 0.0;
                    geometryList[7]->material.reflection = 0.0;
                    geometryList[7]->material.refraction = 0.0;
                    geometryList[7]->material.diffuse = 0.7;
#ifdef __CLION
                    geometryList[7]->material.texture_ptr =
                            std::make_shared<Texture>(
                                    "/Users/hshen/CLionProject/Assignment3/Texture/floor.tga");
#else
                    geometryList[7]->material.texture_ptr =
                            std::make_shared<Texture>("./Texture/floor.tga");
#endif
                    geometryList[7]->material.textureScaleFactor = 5.0;

                    // left Plane
                    geometryList[8] = new Plane(Math::Vec3(-1, 0, 0), 7.0f);
                    geometryList[8]->material.specular = 0.0;
                    geometryList[8]->material.reflection = 0.0;
                    geometryList[8]->material.refraction = 0.0;
                    geometryList[8]->material.diffuse = 0.8;
#ifdef __CLION
                    geometryList[8]->material.texture_ptr = std::make_shared<Texture>(
                            "/Users/hshen/CLionProject/Assignment3/Texture/ceiling__tiles.tga");
#else
                    geometryList[8]->material.texture_ptr = std::make_shared<Texture>(
                        "./Texture/ceiling__tiles.tga");
#endif
                    geometryList[8]->material.textureScaleFactor = 5.0;

                    // small sphere
                    geometryList[9] = new Sphere(Math::Vec3(-5.8, -3.0f, 5),
                                                 1.2);
                    geometryList[9]->material.reflection = 0.0;
                    geometryList[9]->material.refraction = 0.0;
                    geometryList[9]->material.refractiveIndex = 1.3;
                    geometryList[9]->material.diffuse = 0.9;
                    geometryList[9]->material.setColor(
                            Math::Vec3(85.0 / 256.0, 219.0 / 256.0,
                                       225.0 / 256.0));

                    // middle sphere2
                    geometryList[10] = new Sphere(Math::Vec3(4, -3.0f, 4), 1);
                    geometryList[10]->material.reflection = 0.0;
                    geometryList[10]->material.refraction = 0.0;
                    geometryList[10]->material.refractiveIndex = 1.3;
                    geometryList[10]->material.diffuse = 0.9;
                    geometryList[10]->material.specular = 0.2;
                    geometryList[10]->material.setColor(
                            Math::Vec3(251.0 / 256.0, 183.0 / 256.0,
                                       21.0 / 256.0));

                    numberOfGeometries = 11;

                    break;
                }
                case 3: {

                    lengthOfGeometryList = 50;
                    geometryList = new Geometry *[lengthOfGeometryList];

                    // ground Plane
                    geometryList[0] = new Plane(Math::Vec3(0, 1, 0), 4.4f);
                    geometryList[0]->material.reflection = 0.6;
                    geometryList[0]->material.specular = 0.8;
                    geometryList[0]->material.refraction = 0.0;
                    geometryList[0]->material.diffuse = 1.0;
                    geometryList[0]->material.setColor(
                            Math::Vec3(0.3f, 0.3f, 0.3f));

                    // back Plane
                    geometryList[1] = new Plane(Math::Vec3(0, 0, -1), 12.4f);
                    geometryList[1]->material.specular = 0.4;
                    geometryList[1]->material.reflection = 0.0;
                    geometryList[1]->material.refraction = 0.0;
                    geometryList[1]->material.diffuse = 1.0;
#ifdef __CLION
                    geometryList[1]->material.texture_ptr = std::make_shared<Texture>(
                            "/Users/hshen/CLionProject/Assignment3/Texture/bricks_red.tga");
#else
                    geometryList[1]->material.texture_ptr = std::make_shared<Texture>(
                        "./Texture/bricks_red.tga");
#endif
                    geometryList[1]->material.textureScaleFactor = 10.0;

                    // small sphere
                    geometryList[2] = new Sphere(Math::Vec3(-5, -2.7f, 9), 1.5);
                    geometryList[2]->material.reflection = 0.8;
                    geometryList[2]->material.refraction = 0.0;
                    geometryList[2]->material.refractiveIndex = 1.3;
                    geometryList[2]->material.diffuse = 0.1;
                    geometryList[2]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));
                    geometryList[2]->material.diffuseReflection = 0.1;

                    // middle sphere
                    geometryList[3] = new Sphere(Math::Vec3(4, -3.2f, 7), 1);
                    geometryList[3]->material.reflection = 0.1;
                    geometryList[3]->material.refraction = 0.9;
                    geometryList[3]->material.refractiveIndex = 1.5;
                    geometryList[3]->material.diffuse = 0.1;
                    geometryList[3]->material.specular = 0.2;
                    geometryList[3]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));
                    geometryList[3]->material.diffuseReflection = 0.1;

                    // light source
                    geometryList[4] = new Sphere(Math::Vec3(0, 4.8, 5), 0.1f);
                    geometryList[4]->Light(true);
                    geometryList[4]->material.setColor(Math::Vec3(1, 1, 1));

                    // big middle sphere
                    geometryList[5] = new Sphere(Math::Vec3(0, -1.7f, 7), 2.0);
                    geometryList[5]->material.reflection = 0.1;
                    geometryList[5]->material.refraction = 0.9;
                    geometryList[5]->material.refractiveIndex = 1.3;
                    geometryList[5]->material.diffuse = 0.1;
                    geometryList[5]->material.setColor(
                            Math::Vec3(1.0f, 1.0f, 1.0f));
                    geometryList[5]->material.diffuseReflection = 0.1;

                    // ceiling
                    geometryList[6] = new Plane(Math::Vec3(0, -1, 0), 5.2f);
                    geometryList[6]->material.specular = 0.8;
                    geometryList[6]->material.reflection = 0.0;
                    geometryList[6]->material.refraction = 0.0;
                    geometryList[6]->material.diffuse = 1.0;
                    geometryList[6]->material.setColor(
                            Math::Vec3(0.4f, 0.3f, 0.3f));

                    // right Plane
                    geometryList[7] = new Plane(Math::Vec3(1, 0, 0), 7.0f);
                    geometryList[7]->material.specular = 0.0;
                    geometryList[7]->material.reflection = 0.0;
                    geometryList[7]->material.refraction = 0.0;
                    geometryList[7]->material.diffuse = 0.7;
#ifdef __CLION
                    geometryList[7]->material.texture_ptr =
                            std::make_shared<Texture>(
                                    "/Users/hshen/CLionProject/Assignment3/Texture/floor.tga");
#else
                    geometryList[7]->material.texture_ptr =
                            std::make_shared<Texture>("./Texture/floor.tga");
#endif
                    geometryList[7]->material.textureScaleFactor = 5.0;

                    // left Plane
                    geometryList[8] = new Plane(Math::Vec3(-1, 0, 0), 7.0f);
                    geometryList[8]->material.specular = 0.0;
                    geometryList[8]->material.reflection = 0.0;
                    geometryList[8]->material.refraction = 0.0;
                    geometryList[8]->material.diffuse = 0.8;
#ifdef __CLION
                    geometryList[8]->material.texture_ptr = std::make_shared<Texture>(
                            "/Users/hshen/CLionProject/Assignment3/Texture/ceiling__tiles.tga");
#else
                    geometryList[8]->material.texture_ptr = std::make_shared<Texture>(
                        "./Texture/ceiling__tiles.tga");
#endif
                    geometryList[8]->material.textureScaleFactor = 5.0;

                    // small sphere
                    geometryList[9] = new Sphere(Math::Vec3(-5.8, -3.0f, 5),
                                                 1.2);
                    geometryList[9]->material.reflection = 0.0;
                    geometryList[9]->material.refraction = 0.0;
                    geometryList[9]->material.refractiveIndex = 1.3;
                    geometryList[9]->material.diffuse = 0.9;
                    geometryList[9]->material.setColor(
                            Math::Vec3(85.0 / 256.0, 219.0 / 256.0,
                                       225.0 / 256.0));

                    // middle sphere2
                    geometryList[10] = new Sphere(Math::Vec3(3, -3.0f, 4), 1);
                    geometryList[10]->material.reflection = 0.0;
                    geometryList[10]->material.refraction = 0.0;
                    geometryList[10]->material.refractiveIndex = 1.3;
                    geometryList[10]->material.diffuse = 0.9;
                    geometryList[10]->material.specular = 0.2;
                    geometryList[10]->material.setColor(
                            Math::Vec3(251.0 / 256.0, 183.0 / 256.0,
                                       21.0 / 256.0));

                    numberOfGeometries = 11;

                    break;
                }
                default: {

                    // We give enough space since we need to
                    // load a triangle mesh here.
                    lengthOfGeometryList = 500000;
                    geometryList = new Geometry *[lengthOfGeometryList];

                    // ground Plane
                    geometryList[0] = new Plane(Math::Vec3(0, 1, 0), 4.4f);
                    geometryList[0]->material.reflection = 0.6;
                    geometryList[0]->material.specular = 0.8;
                    geometryList[0]->material.refraction = 0.0;
                    geometryList[0]->material.diffuse = 1.0;
                    geometryList[0]->material.setColor(
                            Math::Vec3(0.3f, 0.3f, 0.3f));

                    // back Plane
                    geometryList[1] = new Plane(Math::Vec3(0, 0, -1), 12.4f);
                    geometryList[1]->material.specular = 0.4;
                    geometryList[1]->material.reflection = 0.0;
                    geometryList[1]->material.refraction = 0.0;
                    geometryList[1]->material.diffuse = 1.0;
                    geometryList[1]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 0.7f));

                    // small sphere
                    geometryList[2] = new Sphere(Math::Vec3(-5, -2.7f, 9), 1.5);
                    geometryList[2]->material.reflection = 0.8;
                    geometryList[2]->material.refraction = 0.0;
                    geometryList[2]->material.refractiveIndex = 1.3;
                    geometryList[2]->material.diffuse = 0.1;
                    geometryList[2]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));
                    geometryList[2]->material.diffuseReflection = 0.1;

                    // middle sphere
                    geometryList[3] = new Sphere(Math::Vec3(4, -3.2f, 7), 1);
                    geometryList[3]->material.reflection = 0.1;
                    geometryList[3]->material.refraction = 0.9;
                    geometryList[3]->material.refractiveIndex = 1.5;
                    geometryList[3]->material.diffuse = 0.1;
                    geometryList[3]->material.specular = 0.2;
                    geometryList[3]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));
                    geometryList[3]->material.diffuseReflection = 0.1;

                    // light source
                    geometryList[4] = new Sphere(Math::Vec3(0, 4.8, 5), 0.1f);
                    geometryList[4]->Light(true);
                    geometryList[4]->material.setColor(Math::Vec3(1, 1, 1));

                    // middle sphere2
                    geometryList[5] = new Sphere(Math::Vec3(4, -3.0f, 4), 1);
                    geometryList[5]->material.reflection = 0.0;
                    geometryList[5]->material.refraction = 0.0;
                    geometryList[5]->material.refractiveIndex = 1.3;
                    geometryList[5]->material.diffuse = 0.9;
                    geometryList[5]->material.specular = 0.2;
                    geometryList[5]->material.setColor(
                            Math::Vec3(0.7f, 0.3f, 1.0f));

                    // ceiling
                    geometryList[6] = new Plane(Math::Vec3(0, -1, 0), 5.2f);
                    geometryList[6]->material.specular = 0.8;
                    geometryList[6]->material.reflection = 0.0;
                    geometryList[6]->material.refraction = 0.0;
                    geometryList[6]->material.diffuse = 1.0;
                    geometryList[6]->material.setColor(
                            Math::Vec3(0.4f, 0.3f, 0.3f));

                    // right Plane
                    geometryList[7] = new Plane(Math::Vec3(1, 0, 0), 7.0f);
                    geometryList[7]->material.specular = 0.0;
                    geometryList[7]->material.reflection = 0.0;
                    geometryList[7]->material.refraction = 0.0;
                    geometryList[7]->material.diffuse = 0.7;
                    geometryList[7]->material.setColor(
                            Math::Vec3(0.12f, 0.45f, 0.15f));

                    // left Plane
                    geometryList[8] = new Plane(Math::Vec3(-1, 0, 0), 7.0f);
                    geometryList[8]->material.specular = 0.0;
                    geometryList[8]->material.reflection = 0.0;
                    geometryList[8]->material.refraction = 0.0;
                    geometryList[8]->material.diffuse = 0.8;
                    geometryList[8]->material.setColor(
                            Math::Vec3(0.65f, 0.05f, 0.05f));

                    // small sphere
                    geometryList[9] = new Sphere(Math::Vec3(-5.8, -3.0f, 5),
                                                 1.2);
                    geometryList[9]->material.reflection = 0.0;
                    geometryList[9]->material.refraction = 0.0;
                    geometryList[9]->material.refractiveIndex = 1.3;
                    geometryList[9]->material.diffuse = 0.9;
                    geometryList[9]->material.setColor(
                            Math::Vec3(0.7f, 0.7f, 1.0f));

                    numberOfGeometries = 10;

                    TriangleMesh Teapot(Math::Vec3(0, -2.0, 6.0), 1);
#ifdef __CLION
                    Teapot.objLoader(
                            "/Users/hshen/CLionProject/Assignment3/Samples/teapot_triangulated.obj",
                            geometryList, numberOfGeometries);
#else
                    Teapot.objLoader(
                        "./Samples/teapot_triangulated.obj",
                        geometryList, numberOfGeometries);
#endif
                    auto teapot_material_ptr = new Material;
                    teapot_material_ptr->setColor(Math::Vec3(0.48, 0.46, 0.42));
                    teapot_material_ptr->diffuseReflection = 0.0;
                    teapot_material_ptr->diffuse = 2.0;
                    teapot_material_ptr->reflection = 0.0;
                    teapot_material_ptr->refraction = 0.0;
                    teapot_material_ptr->refractiveIndex = 1.5;
                    teapot_material_ptr->specular = 0.0;
                    Teapot.setMaterial(teapot_material_ptr);

                    break;
                }
            }

            // Initialize the grid for spatial subdivisions
            gridInitialization();
        }

        void gridInitialization() {

            // Initialize regular grid by length * width * height
            grid = new std::vector<Geometry *>[GRID_SIZE * GRID_SIZE *
                                               GRID_SIZE];

            // Define the minimal component of each direction of the grid as a vector
            Math::Vec3 p_min(MINIMAL_COMPONENT, MINIMAL_COMPONENT,
                             MINIMAL_COMPONENT);

            // Define the maximal component of each direction of the grid as a vector
            Math::Vec3 p_max(MAXIMAL_COMPONENT, MAXIMAL_COMPONENT,
                             MAXIMAL_COMPONENT);

            // Create an AABB for the grid
            boundary = Bounding_box(p_min, p_max - p_min);

            // Obtain the width, height and depth of a cell
            double cell_width = (p_max.x - p_min.x) / GRID_SIZE;
            double cell_height = (p_max.y - p_min.y) / GRID_SIZE;
            double cell_depth = (p_max.z - p_min.z) / GRID_SIZE;

            // Initialize the light list
            lightList = new Geometry *[MAX_LIGHTS];

            // Store 'Geometry' instances that are light to lightList, meanwhile,
            // store all 'Geometry' instances in the grid cells.
            for (int i = 0; i < numberOfGeometries; ++i) {
                if (geometryList[i]->isLight) {
                    lightList[numberOfLights++] = geometryList[i];
                }
                // Create an AABB for each geometry
                Bounding_box boundingBox = geometryList[i]->getBoundingBox();

                // Obtain the minimal and the maximal component of the size of the bounding box
                Math::Vec3 b_min = boundingBox.minimum;
                Math::Vec3 b_max = b_min + boundingBox.size;

                // Find out all cells that can contain the geometries based on boundingBox
                // by calculating the lower bound and the upper bound in x-axis, y-axis, z-axis.
                int x_lowerbound = static_cast<int>((b_min.x - p_min.x) /
                                                    cell_width);
                int x_upperbound =
                        static_cast<int> ((b_max.x - p_min.x) / cell_width) + 1;

                // Make sure x_lowerbound >= 0
                if (x_lowerbound < 0) {
                    x_lowerbound = 0;
                }
                // Make sure x_upperbound <=  GRID_SIZE - 1
                if (x_upperbound > GRID_SIZE - 1) {
                    x_upperbound = GRID_SIZE - 1;
                }

                int y_lowerbound = static_cast<int> ((b_min.y - p_min.y) /
                                                     cell_height);
                int y_upperbound =
                        static_cast<int> ((b_max.y - p_min.y) / cell_height) +
                        1;

                // Make sure y_lowerbound >= 0
                if (y_lowerbound < 0) {
                    y_lowerbound = 0;
                }

                // Make sure y_lowerbound <= GRID_SIZE - 1
                if (y_upperbound > GRID_SIZE - 1) {
                    y_upperbound = GRID_SIZE - 1;
                }

                int z_lowerbound = static_cast<int> ((b_min.z - p_min.z) /
                                                     cell_depth);
                int z_upperbound =
                        static_cast<int> ((b_max.z - p_min.z) / cell_depth) + 1;

                // Make sure z_lowerbound >= 0
                if (z_lowerbound < 0) {
                    z_lowerbound = 0;
                }

                // Make sure z_upperbound <= GRID_SIZE - 1
                if (z_upperbound > GRID_SIZE - 1) {
                    z_upperbound = GRID_SIZE - 1;
                }

                // Iterate all candidate cells
                for (int x = x_lowerbound; x < x_upperbound; ++x)
                    for (int y = y_lowerbound; y < y_upperbound; ++y)
                        for (int z = z_lowerbound; z < z_upperbound; ++z) {

                            // For each cell, create a bounding box
                            int idx = x + y * GRID_SIZE +
                                      z * GRID_SIZE * GRID_SIZE;
                            Math::Vec3 pos(p_min.x + x * cell_width,
                                           p_min.y + y * cell_height,
                                           p_min.z + z * cell_depth);
                            Math::Vec3 temp(cell_width, cell_height,
                                            cell_depth);
                            Bounding_box cell(pos, temp);

                            // If the ray hits the cell where the geometry is,
                            // put the geometry into the cell.
                            if (geometryList[i]->hitBoundingBox(cell)) {
                                grid[idx].push_back(geometryList[i]);
                            }
                        }
            }
        }

        // Getters
        std::vector<Geometry *> *getGrid() { return grid; }

        Bounding_box &getBoundaryReference() { return boundary; }

        // Fields
        int numberOfGeometries;         // Number of geometries in the scene
        int numberOfLights;             // Number of lights among the geometries
        int lengthOfGeometryList;       // Length of geometry list (including those triangles from triangle meshes)
        Geometry **geometryList;        // Pointer to the list of geometries
        Geometry **lightList;           // Pointer to the list of lights
        Bounding_box boundary;          // AABB that bounds the scene
        std::vector<Geometry *> *grid;  // The grid array for spatial subdivisions
    };

    struct RenderingEngine {

        // No default constructor
        RenderingEngine() = delete;

        // Constructor
        RenderingEngine(int screenWidth, int screenHeight) : scene(new Scene()),
                                                             screen_width(
                                                                     screenWidth),
                                                             screen_height(
                                                                     screenHeight) {
        }

        // Destructor
        ~RenderingEngine() {
            delete scene;
        }

        // Set the unit of length before creating the grid
        void setUnitLength() {

            // Obtain the number of grids in a unit length of x-axis
            numberOfGrids.x = GRID_SIZE / scene->getBoundaryReference().size.x;

            // Obtain the number of grids in a unit length of y-axis
            numberOfGrids.y = GRID_SIZE / scene->getBoundaryReference().size.y;

            // Obtain the number of grids in a unit length of z-axis
            numberOfGrids.z = GRID_SIZE / scene->getBoundaryReference().size.z;

            // Obtain the size of a unit grid by re-calculation (in order to reduce loss of precision)
            sizeOfUnitGrid = scene->getBoundaryReference().size / GRID_SIZE;
        }

        Geometry *
        Raytrace(Ray &ray, Math::Vec3 &currentColor, int depth,
                 double refractiveIndex,
                 double &distance, double sample, double rangeOfSample) {

            // Base case 1: If the current depth exceeds the maximal depth allowed, stop tracing
            if (depth > MAXIMAL_DEPTH_TO_TRACE) {
                return nullptr;
            }

            // Start tracing primary ray (primary rays are those rays hit light sources
            // or other geometries without bounces or refractions)
            // Make the distance to be infinity at first, then we
            // reduce it by using hit function
            distance = INFINITY_;
            Math::Vec3 positionOfIntersection;      // the vector to store the position of point of intersection
            Geometry *geometry_ptr = nullptr;   // The pointer to geometry being hit

            // Check if there is a nearest intersection
            auto result = static_cast<HIT_STATUS>(findTheNearestIntersection(
                    ray,
                    distance,
                    geometry_ptr));

            // Base case 2: Stop tracing if there is no intersection
            if (result == HIT_STATUS::NoIntersection || !geometry_ptr) {
                return nullptr;
            }

            // We are going to calculate the nearest intersection and its distance to the eye of the current ray
            // If there is an intersection, then
            // Case 1: If the object being hit is a light, stop tracing and return the color of it
            if (geometry_ptr->isLight) {
                currentColor += Math::Vec3(1.0, 1.0, 1.0);
            }
                // Case 2: Otherwise the object is not a light
            else {

                // Obtain the position of the point of intersection
                positionOfIntersection = ray.origin + ray.direction * distance;

                // Re-iterate all lights, add Diffuse + Specular from each light to the ray color
                // since lights have no reflection
                for (int lightIndex = 0;
                     lightIndex < scene->numberOfLights; ++lightIndex) {

                    Geometry *light = scene->lightList[lightIndex];
                    Math::Vec3 L;
                    double shade = calculateTheShade(light,
                                                     positionOfIntersection,
                                                     L, sample,
                                                     rangeOfSample);
                    // Obtain the normal vector of the intersection
                    Math::Vec3 N = geometry_ptr->getNormal(
                            positionOfIntersection);

                    // Diffuse:
                    if (geometry_ptr->material.diffuse > 0) {
                        double dot = Math::dot(L, N);
                        if (dot > 0) {
                            double diff =
                                    dot * geometry_ptr->material.diffuse *
                                    shade;
                            // Add the diffuse component to ray color
                            currentColor +=
                                    diff * light->material.color *
                                    geometry_ptr->getColor(
                                            positionOfIntersection);
                        }
                    }
                    // Specular:
                    if (geometry_ptr->material.specular > 0) {
                        Math::Vec3 V = ray.direction;
                        // Obtain the reflected vector
                        Math::Vec3 R = L - 2.0 * Math::dot(L, N) * N;
                        double dot = Math::dot(V, R);
                        if (dot > 0) {
                            // Add specular component to ray color
                            currentColor += pow(dot, 20.0) *
                                            geometry_ptr->material.specular *
                                            shade * light->material.color;
                        }
                    }
                }

                // Reflection:
                double reflection = geometry_ptr->material.reflection;
                if (reflection > 0.0) {

                    Math::Vec3 N = geometry_ptr->getNormal(
                            positionOfIntersection);
                    Math::Vec3 R = ray.direction -
                                   2.0 * Math::dot(ray.direction, N) * N;

                    // Obtain the diffuse reflection
                    if (geometry_ptr->material.diffuseReflection > 0 &&
                        depth < 2) {
                        Math::Vec3 factor1 = Math::Vec3(R.z, 0.0, -R.x);
                        Math::Vec3 factor2 = Math::normalize(
                                Math::cross(R, factor1));
                        for (int i = 0; i < 20; i++) {
                            double x0, y0;
                            std::tie(x0, y0) = Utility::getRandomDoublePair();
                            x0 = x0 * geometry_ptr->material.diffuseReflection;
                            y0 = y0 * geometry_ptr->material.diffuseReflection;
                            Math::Vec3 newRay = Math::normalize(
                                    R + factor1 * x0 + factor2 * y0);
                            double dist;
                            Math::Vec3 newRayColor(0.0, 0.0, 0.0);
                            auto tempRay = Ray(
                                    positionOfIntersection + newRay * EPSILON,
                                    newRay);
                            Raytrace(tempRay, newRayColor, depth + 1,
                                     refractiveIndex,
                                     dist,
                                     sample * 0.25, rangeOfSample * 4.0);

                            currentColor +=
                                    reflection * newRayColor *
                                    geometry_ptr->getColor(
                                            positionOfIntersection) /
                                    20;
                        }
                    } else if (depth < MAXIMAL_DEPTH_TO_TRACE) {
                        Math::Vec3 rcol(0, 0, 0);
                        double dist;
                        auto tempRay = Ray(positionOfIntersection + R * EPSILON,
                                           R);
                        Raytrace(tempRay, rcol, depth + 1, refractiveIndex,
                                 dist,
                                 sample * 0.25, rangeOfSample * 4.0);

                        currentColor += reflection * rcol *
                                        geometry_ptr->getColor(
                                                positionOfIntersection);
                    }
                }
                // Refraction
                double refr = geometry_ptr->material.refraction;
                if (refr > 0.0 && depth < MAXIMAL_DEPTH_TO_TRACE) {
                    Math::Vec3 rcol(0.0, 0.0, 0.0);
                    double dist;
                    Math::Vec3 N = geometry_ptr->getNormal(
                            positionOfIntersection);
                    if (result == HIT_STATUS::Inside) {
                        N = -N;
                    }
                    // Snell's law
                    double tmp_refractiveIndex = geometry_ptr->material.refractiveIndex;
                    if (result == HIT_STATUS::Inside) {
                        tmp_refractiveIndex = 1.0;
                    }
                    double ni_over_nt = refractiveIndex / tmp_refractiveIndex;
                    Math::Vec3 V = ray.direction;
                    double cosi = -Math::dot(V, N);
                    double sinr2 =
                            (1.0 - cosi * cosi) * ni_over_nt * ni_over_nt;

                    if (0.0 < sinr2 && sinr2 < 1.0) {
                        double cosr2 = 1.0 - sinr2;

                        Math::Vec3 T = (V * ni_over_nt) +
                                       (ni_over_nt * cosi - sqrt(cosr2)) * N;

                        auto temp1 = positionOfIntersection + T * EPSILON;
                        auto temp2 = T;
                        auto tempRay = Ray(temp1, temp2);

                        Raytrace(tempRay, rcol, depth + 1, tmp_refractiveIndex,
                                 dist,
                                 sample * 0.25, rangeOfSample * 4.0);

                        // Beer–Lambert law: A = c * e * l (from Wiki)
                        Math::Vec3 absorptivity =
                                geometry_ptr->getColor(positionOfIntersection) *
                                0.15 * (-dist);
                        Math::Vec3 transparency(std::exp(absorptivity.r()),
                                                std::exp(absorptivity.g()),
                                                std::exp(absorptivity.b()));
                        currentColor += rcol * transparency;
                    }
                }
            }
            return geometry_ptr;
        }

        // Set the camera, start rendering and output the result
        void render(int renderingMode) {

            // renderingMode:
            // 1: super-sampling, no depth of field
            // 2: no super-sampling, no depth of field
            // 3: depth of field, no super-sampling
            if (renderingMode < 1 || renderingMode > 5) {
                fprintf(stderr, "Invalid rendering mode!\n");
                exit(EXIT_SUCCESS);
            }

            // Output as a .ppm file
            // P3 means we are using RGB color mode
            // 255 means the 255 is the maximal of value (0-255 is mapping to 0.0-1.0) of RGB value we are gonna use
            std::cout << "P3\n" << screen_width << " " << screen_height
                      << "\n255\n";

            // Set the camera
            Math::Vec3 eye(0.0, 0.0, -5.0);
            Math::Vec3 target(0.0, 0.0, 0.0);
            Math::Vec3 upVector(0.0, 1.0, 0.0);
            double distanceToProjection(5.0);
            double ratio(0.01);
            double width = screen_width * ratio;
            double height = screen_height * ratio;
            double radiusOfAperture = 0.0;
            double distanceToFocalPlane = 0.0;

            // Set the aperture and the distance to the focal plane if depth of field is used
            if (renderingMode == 3) {
                radiusOfAperture = 0.3;
                distanceToFocalPlane = 9.0;
            }
            Camera camera = Camera(
                    eye,
                    target,
                    upVector,
                    distanceToProjection,
                    width,
                    height,
                    ratio,
                    radiusOfAperture,
                    distanceToFocalPlane
            );

            // three variables to store the RGB values
            int red;
            int green;
            int blue;
            // Start rendering
            for (int y = screen_height - 1; y >= 0; --y) {
                for (int x = 0; x < screen_width; ++x) {
                    // a variable to store the color vector
                    Math::Vec3 color(0.0, 0.0, 0.0);
                    switch (renderingMode) {
                        case 1: {
                            for (int i = -10; i < 15; i += 10)
                                for (int j = -10; j < 15; j += 10) {
                                    Math::Vec3 dir = Math::normalize(
                                            camera.getDirection
                                                    (x + i / 30.0,
                                                     y + j / 30.0));
                                    Ray r(camera.eye, dir);
                                    double dist;
                                    Raytrace(r, color, 1, 1.0, dist,
                                             SAMPLES,
                                             (1.0 / SAMPLES));
                                }
                            std::tie(red, green, blue) = Utility::rgb2Int(color,
                                                                          SUPER_SAMPLING);
                            break;
                        }
                        case 2: {
                            Ray r(camera.eye,
                                  Math::normalize(camera.getDirection(x, y)));
                            double dist;
                            Raytrace(r, color, 1, 1.0, dist, SAMPLES,
                                     (1.0 / SAMPLES));
                            std::tie(red, green, blue) = Utility::rgb2Int(
                                    color);
                            break;
                        }
                        default: {
                            Ray r(camera.eye,
                                  Math::normalize(camera.getDirection(x, y)));
                            double dist;
                            Raytrace(r, color, 1, 1.0, dist, SAMPLES,
                                     (1.0 / SAMPLES));

                            for (int i = 0; i < 9; ++i) {
                                auto tempRay = camera.getRandomRay(x, y);
                                Raytrace(tempRay, color, 1, 1.0, dist, SAMPLES,
                                         (1.0 / SAMPLES));
                            }
                            color *= 0.1;
                            std::tie(red, green, blue) = Utility::rgb2Int(
                                    color);
                            break;
                        }
                    }
                    // Output the color
                    std::cout << red << ' ' << green << ' ' << blue << '\n';
                }
            }
        }

        double
        calculateTheShade(Geometry *current_light_ptr,
                          Math::Vec3 currentPosition,
                          Math::Vec3 &currentDirection,
                          double currentSample, double currentSampleRange) {

            double shade = 0.0;
            Geometry *geometry_ptr = nullptr;

            // The only type of light in our scene is sphere (point source light)
            auto light = (Sphere *) current_light_ptr;
            Math::Vec3 lightCenter = light->getCenterReference();
            currentDirection = Math::normalize(lightCenter - currentPosition);

            Math::Vec3 dir = lightCenter - currentPosition;
            double dist = Math::norm(dir);
            dir = Math::normalize(dir);

            auto tempRay = Ray(currentPosition + dir * EPSILON, dir);
            if (findTheNearestIntersection(tempRay, dist, geometry_ptr) &&
                geometry_ptr == current_light_ptr) {
                shade += 1.0;
            }
            return shade;
        }

        int findTheNearestIntersection(Ray &currentRay, double &currentDistance,
                                       Geometry *&geometry_ptr) {

            int returnValue = 0;
            Math::Vec3 rayDirection = currentRay.direction;
            Math::Vec3 currentPosition = currentRay.origin;
            auto boundaryRef = scene->getBoundaryReference();

            // Start grid traversal by using 3D version of the DDA algorithm
            Math::Vec3 cb, tMax, tDelta, cell;
            cell = (currentPosition - boundaryRef.minimum) * numberOfGrids;

            int stepX = static_cast<int>(cell.x);
            int X = stepX;
            int outX;

            int stepY = static_cast<int>(cell.y);
            int Y = stepY;
            int outY;

            int stepZ = static_cast<int>(cell.z);
            int Z = stepZ;
            int outZ;

            // Return 0 (no intersection) if one of X, Y, Z is out of range
            if (X < 0 || X >= GRID_SIZE) {
                return 0;
            }
            if (Y < 0 || Y >= GRID_SIZE) {
                return 0;
            }
            if (Z < 0 || Z >= GRID_SIZE) {
                return 0;
            }

            if (rayDirection.x > 0) {
                stepX = 1;
                outX = GRID_SIZE;
                cb.x = boundaryRef.minimum.x + (X + 1) * sizeOfUnitGrid.x;
            } else {
                stepX = -1;
                outX = -1;
                cb.x = boundaryRef.minimum.x + X * sizeOfUnitGrid.x;
            }
            if (rayDirection.y > 0.0f) {
                stepY = 1;
                outY = GRID_SIZE;
                cb.y = boundaryRef.minimum.y + (Y + 1) * sizeOfUnitGrid.y;
            } else {
                stepY = -1;
                outY = -1;
                cb.y = boundaryRef.minimum.y + Y * sizeOfUnitGrid.y;
            }
            if (rayDirection.z > 0.0f) {
                stepZ = 1;
                outZ = GRID_SIZE;
                cb.z = boundaryRef.minimum.z + (Z + 1) * sizeOfUnitGrid.z;
            } else {
                stepZ = -1;
                outZ = -1;
                cb.z = boundaryRef.minimum.z + Z * sizeOfUnitGrid.z;
            }
            double rxr, ryr, rzr;
            if (rayDirection.x != 0) {
                rxr = 1.0 / rayDirection.x;
                tMax.x = (cb.x - currentPosition.x) * rxr;
                tDelta.x = sizeOfUnitGrid.x * stepX * rxr;
            } else {
                tMax.x = INFINITY_;
            }
            if (rayDirection.y != 0) {
                ryr = 1.0f / rayDirection.y;
                tMax.y = (cb.y - currentPosition.y) * ryr;
                tDelta.y = sizeOfUnitGrid.y * stepY * ryr;
            } else {
                tMax.y = INFINITY_;
            }
            if (rayDirection.z != 0) {
                rzr = 1.0f / rayDirection.z;
                tMax.z = (cb.z - currentPosition.z) * rzr;
                tDelta.z = sizeOfUnitGrid.z * stepZ * rzr;
            } else {
                tMax.z = INFINITY_;
            }

            // Initialize stepping
            std::vector<Geometry *> list;
            auto grid = scene->getGrid();
            geometry_ptr = nullptr;
            bool intersected = false;

            // Trace primary ray
            while (true) {
                list = grid[X + Y * 256 + Z * 65536];
                unsigned int list_index = 0;
                while (list_index < list.size()) {
                    Geometry *pr = list[list_index];
                    HIT_STATUS result = pr->hit(currentRay, currentDistance);
                    if (result != HIT_STATUS::NoIntersection) {
                        returnValue = static_cast<int>(result);
                        geometry_ptr = pr;
                        intersected = true;
                        break;
                    }
                    ++list_index;
                }
                if (intersected) {
                    break;
                }
                if (tMax.x < tMax.y) {
                    if (tMax.x < tMax.z) {
                        X += stepX;
                        if (X == outX) {
                            return static_cast<int>(HIT_STATUS::NoIntersection);
                        }
                        tMax.x += tDelta.x;
                    } else {
                        Z += stepZ;
                        if (Z == outZ) {
                            return static_cast<int>(HIT_STATUS::NoIntersection);
                        }
                        tMax.z += tDelta.z;
                    }
                } else {
                    if (tMax.y < tMax.z) {
                        Y += stepY;
                        if (Y == outY) {
                            return static_cast<int>(HIT_STATUS::NoIntersection);
                        }
                        tMax.y += tDelta.y;
                    } else {
                        Z += stepZ;
                        if (Z == outZ) {
                            return static_cast<int>(HIT_STATUS::NoIntersection);
                        }
                        tMax.z += tDelta.z;
                    }
                }
            }

            while (true) {
                list = grid[X + Y * 256 + Z * 65536];
                size_t list_index = 0;
                while (list_index < list.size()) {
                    Geometry *pr = list[list_index];
                    HIT_STATUS result = pr->hit(currentRay, currentDistance);
                    if (result != HIT_STATUS::NoIntersection) {
                        geometry_ptr = pr;
                        returnValue = static_cast<int>(result);
                    }
                    ++list_index;
                }
                if (tMax.x < tMax.y) {
                    if (tMax.x < tMax.z) {
                        if (currentDistance < tMax.x) {
                            break;
                        }
                        X += stepX;
                        if (X == outX) {
                            break;
                        }
                        tMax.x += tDelta.x;
                    } else {
                        if (currentDistance < tMax.z) {
                            break;
                        }
                        Z += stepZ;
                        if (Z == outZ) {
                            break;
                        }
                        tMax.z += tDelta.z;
                    }
                } else {
                    if (tMax.y < tMax.z) {
                        if (currentDistance < tMax.y) {
                            break;
                        }
                        Y += stepY;
                        if (Y == outY) {
                            break;
                        }
                        tMax.y += tDelta.y;
                    } else {
                        if (currentDistance < tMax.z) {
                            break;
                        }
                        Z += stepZ;
                        if (Z == outZ) {
                            break;
                        }
                        tMax.z += tDelta.z;
                    }
                }
            }
            return returnValue;
        }

        // Fields
        Scene *scene;
        int screen_width;
        int screen_height;
        Math::Vec3 numberOfGrids;
        Math::Vec3 sizeOfUnitGrid;
    };
}

// Print the usage if the arguments provided are not valid.
void printUsage() {
    fprintf(stdout, "\nUsage: ./assignment3.out SceneNumber\n\n"
                    "Scene: \n"
                    "1. Balls, Cornell Box, Tetrahedron(Triangles)\n"
                    "2. Balls, Textures\n"
                    "3. Balls, Textures, Depth of field(compare to Scene 2)\n"
                    "4. Balls, Cornell Box, Teapot without interpolation\n"
                    "5. Balls, Cornell Box, Tetrahedron(Triangles) (Without anti-aliasing)\n\n");
}

/**
 * Validate all arguments provided, prompt the usage and exit the program if any invalid argument detected.
 * @param argc the number of arguments
 * @param argv the string array of arguments
 */
void argumentsValidation(int argc, char *argv[], int &sceneNumber) {

    // Check the number of arguments
    if (argc != 2) {
        printUsage();
        exit(EXIT_SUCCESS);
    }

    // Validate the 2nd argument
    for (int i = 1; i <= 5; ++i) {
        if (std::to_string(i) == argv[1]) {
            sceneNumber = std::stoi(argv[1]);
            return;
        }
    }
    // Print the error message and abort the program if the second argument is not correct.
    printUsage();
    exit(EXIT_SUCCESS);
}

/**
 * the 'main' function.
 * @param argc the number of arguments
 * @param argv the string array of arguments
 * @return 0
 */
int main(int argc, char *argv[]) {

    int sceneNumber;
    std::string outputName;
    std::string outputPrefix;
#ifndef __CLION
    argumentsValidation(argc, argv, sceneNumber);
    outputPrefix = "./Output/";
#else
    sceneNumber = 5;
    outputPrefix = "/Users/hshen/CLionProject/Assignment3/Output/";
#endif

    if (sceneNumber == 5) {
        outputName = "scene" + std::to_string(sceneNumber) + std::string("_no_supersampling") + ".ppm";
    } else {
        outputName = "scene" + std::to_string(sceneNumber) + ".ppm";
    }
    std::string outputPath = outputPrefix + outputName;

    // Show the path of output
    std::cout << "Output to: " << outputPath << std::endl;
    std::cout << "Rendering Scene " << sceneNumber << " ...";

    // Redirect all standard out to the ppm
    freopen(outputPath.c_str(), "w", stdout);

    // Initialization of the renderer
    int screenWidth = Raytracer::DEFAULT_WIDTH;
    int screenHeight = Raytracer::DEFAULT_HEIGHT;
    auto tracerInstance = new Raytracer::RenderingEngine(screenWidth, screenHeight);
    if (sceneNumber == 5) {
        tracerInstance->scene->drawScene(1);
    } else {
        tracerInstance->scene->drawScene(sceneNumber);
    }
    tracerInstance->setUnitLength();
    if (sceneNumber == 3) {
        // Use depth of field for the 3rd scene
        tracerInstance->render(3);
    } else if (sceneNumber == 5) {
        // Don't use super-sampling in the 5th scene
        tracerInstance->render(2);
    } else {
        tracerInstance->render(1);
    }

    // Show that the work is done
    std::cerr << " Done" << std::endl;
    return 0;
}
