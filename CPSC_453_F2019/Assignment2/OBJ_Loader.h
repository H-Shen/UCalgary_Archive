// OBJ_Loader.h - A Single Header OBJ Model Loader
// It is adjusted from https://github.com/Bly7/OBJ-Loader

#pragma once

#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <cmath>

// namespace: OBJLoader
// Description: The namespace that holds everything that
//	is needed and used for the OBJ Model Loader
namespace OBJLoader {
    // Structure: Vector2
    //
    // Description: A 2D Vector that Holds Positional Data
    struct Vector2 {
        // Default Constructor
        Vector2() : Vector2(0.0f, 0.0f) {}

        // Variable Set Constructor
        Vector2(float X_, float Y_) : X(X_), Y(Y_) {}

        // Bool Equals Operator Overload
        bool operator==(const Vector2 &other) const {
            return (this->X == other.X && this->Y == other.Y);
        }

        // Bool Not Equals Operator Overload
        bool operator!=(const Vector2 &other) const {
            return !(this->X == other.X && this->Y == other.Y);
        }

        // Addition Operator Overload
        Vector2 operator+(const Vector2 &right) const {
            return Vector2(this->X + right.X, this->Y + right.Y);
        }

        // Subtraction Operator Overload
        Vector2 operator-(const Vector2 &right) const {
            return Vector2(this->X - right.X, this->Y - right.Y);
        }

        // Float Multiplication Operator Overload
        Vector2 operator*(const float &other) const {
            return Vector2(this->X * other, this->Y * other);
        }

        // Positional Variables
        float X;
        float Y;
    };

    // Structure: Vector3
    //
    // Description: A 3D Vector that Holds Positional Data
    struct Vector3 {
        // Default Constructor
        Vector3() : Vector3(0.0f, 0.0f, 0.0f) {}

        // Variable Set Constructor
        Vector3(float X_, float Y_, float Z_) : X(X_), Y(Y_), Z(Z_) {}

        // Bool Equals Operator Overload
        bool operator==(const Vector3 &other) const {
            return (this->X == other.X && this->Y == other.Y && this->Z == other.Z);
        }

        // Bool Not Equals Operator Overload
        bool operator!=(const Vector3 &other) const {
            return !(this->X == other.X && this->Y == other.Y && this->Z == other.Z);
        }

        // Addition Operator Overload
        Vector3 operator+(const Vector3 &right) const {
            return Vector3(this->X + right.X, this->Y + right.Y, this->Z + right.Z);
        }

        // Subtraction Operator Overload
        Vector3 operator-(const Vector3 &right) const {
            return Vector3(this->X - right.X, this->Y - right.Y, this->Z - right.Z);
        }

        // Float Multiplication Operator Overload
        Vector3 operator*(const float &other) const {
            return Vector3(this->X * other, this->Y * other, this->Z * other);
        }

        // Float Division Operator Overload
        Vector3 operator/(const float &other) const {
            return Vector3(this->X / other, this->Y / other, this->Z / other);
        }

        // Positional Variables
        float X;
        float Y;
        float Z;
    };

    // Structure: Vertex
    //
    // Description: Model Vertex object that holds
    //	a Position, Normal, and Texture Coordinate
    struct Vertex {
        // Position Vector
        Vector3 Position;

        // Normal Vector
        Vector3 Normal;

        // Texture Coordinate Vector
        Vector2 TextureCoordinate;
    };

    // Structure: Mesh
    //
    // Description: A Simple Mesh Object that holds
    //	a name, a vertex list, and an index list
    struct Mesh {
        // Default Constructor
        Mesh() = default;

        // Variable Set Constructor
        Mesh(std::vector<Vertex> &_Vertices, std::vector<unsigned int> &_Indices) {
            Vertices = _Vertices;
            Indices = _Indices;
        }

        // Mesh Name
        std::string MeshName;
        // Vertex List
        std::vector<Vertex> Vertices;
        // Index List
        std::vector<unsigned int> Indices;

        // Material
        //Material MeshMaterial;
    };

    // Namespace: Math
    //
    // Description: The namespace that holds all of the math
    //	functions need for OBJLoader
    namespace Math {
        // Vector3 Cross Product
        Vector3 CrossVector3(const Vector3 a, const Vector3 b) {
            return Vector3(a.Y * b.Z - a.Z * b.Y,
                           a.Z * b.X - a.X * b.Z,
                           a.X * b.Y - a.Y * b.X);
        }

        // Vector3 Magnitude Calculation
        float MagnitudeVector3(const Vector3 in) {
            return (sqrtf(powf(in.X, 2) + powf(in.Y, 2) + powf(in.Z, 2)));
        }

        // Vector3 DotProduct
        float DotVector3(const Vector3 a, const Vector3 b) {
            return (a.X * b.X) + (a.Y * b.Y) + (a.Z * b.Z);
        }

        // Angle between 2 Vector3 Objects
        float AngleBetweenVector3(const Vector3 a, const Vector3 b) {
            float angle = DotVector3(a, b);
            angle /= (MagnitudeVector3(a) * MagnitudeVector3(b));
            return acosf(angle);
        }

        // Projection Calculation of a onto b
        Vector3 ProjectionVector3(const Vector3 a, const Vector3 b) {
            Vector3 bn = b / MagnitudeVector3(b);
            return bn * DotVector3(a, bn);
        }
    }

    // Namespace: Algorithm
    //
    // Description: The namespace that holds all of the
    // Algorithms needed for OBJL
    namespace Algorithm {
        // Vector3 Multiplication Operator Overload
        Vector3 operator*(const float &left, const Vector3 &right) {
            return Vector3(right.X * left, right.Y * left, right.Z * left);
        }

        // A test to see if P1 is on the same side as P2 of a line segment ab
        bool SameSide(Vector3 p1, Vector3 p2, Vector3 a, Vector3 b) {
            Vector3 cp1 = Math::CrossVector3(b - a, p1 - a);
            Vector3 cp2 = Math::CrossVector3(b - a, p2 - a);
            return Math::DotVector3(cp1, cp2) >= 0;
        }

        // Generate a cross product normal for a triangle
        Vector3 GenTriNormal(Vector3 t1, Vector3 t2, Vector3 t3) {
            Vector3 u = t2 - t1;
            Vector3 v = t3 - t1;
            Vector3 normal = Math::CrossVector3(u, v);
            return normal;
        }

        // Check to see if a Vector3 Point is within a 3 Vector3 Triangle
        bool inTriangle(Vector3 point, Vector3 tri1, Vector3 tri2, Vector3 tri3) {
            // Test to see if it is within an infinite prism that the triangle outlines.
            bool within_tri_prism = SameSide(point, tri1, tri2, tri3)
                                    && SameSide(point, tri2, tri1, tri3)
                                    && SameSide(point, tri3, tri1, tri2);

            // If it isn't it will never be on the triangle
            if (!within_tri_prism) {
                return false;
            }

            // Calculate Triangle's Normal
            Vector3 n = GenTriNormal(tri1, tri2, tri3);

            // Project the point onto this normal
            Vector3 proj = Math::ProjectionVector3(point, n);

            // If the distance from the triangle to the point is 0
            //	it lies on the triangle
            return Math::MagnitudeVector3(proj) == 0;
        }

        // Split a String into a string array at a given token
        inline void split(const std::string &in,
                          std::vector<std::string> &out,
                          const std::string &token) {
            out.clear();
            std::string temp;

            for (int i = 0; i < int(in.size()); i++) {
                std::string test = in.substr(static_cast<unsigned long>(i), token.size());

                if (test == token) {
                    if (!temp.empty()) {
                        out.push_back(temp);
                        temp.clear();
                        i += (int) token.size() - 1;
                    } else {
                        out.emplace_back("");
                    }
                } else if (i + token.size() >= in.size()) {
                    temp += in.substr(static_cast<unsigned long>(i), token.size());
                    out.push_back(temp);
                    break;
                } else {
                    temp += in[i];
                }
            }
        }

        // Get tail of string after first token and possibly following spaces
        inline std::string tail(const std::string &in) {
            size_t token_start = in.find_first_not_of(" \t");
            size_t space_start = in.find_first_of(" \t", token_start);
            size_t tail_start = in.find_first_not_of(" \t", space_start);
            size_t tail_end = in.find_last_not_of(" \t");
            if (tail_start != std::string::npos && tail_end != std::string::npos) {
                return in.substr(tail_start, tail_end - tail_start + 1);
            } else if (tail_start != std::string::npos) {
                return in.substr(tail_start);
            }
            return "";
        }

        // Get first token of string
        inline std::string firstToken(const std::string &in) {
            if (!in.empty()) {
                size_t token_start = in.find_first_not_of(" \t");
                size_t token_end = in.find_first_of(" \t", token_start);
                if (token_start != std::string::npos && token_end != std::string::npos) {
                    return in.substr(token_start, token_end - token_start);
                } else if (token_start != std::string::npos) {
                    return in.substr(token_start);
                }
            }
            return "";
        }

        // Get element at given index position
        template<class T>
        inline const T &getElement(const std::vector<T> &elements, std::string &index) {
            int idx = std::stoi(index);
            if (idx < 0) {
                idx = int(elements.size()) + idx;
            } else {
                --idx;
            }
            return elements[idx];
        }
    }

    // Class: Loader
    //
    // Description: The OBJ Model Loader
    class Loader {
    public:
        // Default Constructor
        Loader() = default;

        ~Loader() {
            LoadedMeshes.clear();
        }

        // Load a file into the loader
        //
        // If file is loaded return true
        //
        // If the file is unable to be found
        // or unable to be loaded return false
        bool LoadFile(const std::string &Path) {
            // If the file is not an .obj file return false
            if (Path.substr(Path.size() - 4, 4) != ".obj")
                return false;


            std::ifstream file(Path);

            if (!file.is_open())
                return false;

            LoadedMeshes.clear();
            LoadedVertices.clear();
            LoadedIndices.clear();

            std::vector<Vector3> Positions;
            std::vector<Vector2> TCoords;
            std::vector<Vector3> Normals;

            std::vector<Vertex> Vertices;
            std::vector<unsigned int> Indices;

            std::vector<std::string> MeshMatNames;

            bool listening = false;
            std::string meshName;

            Mesh tempMesh;
            std::string currentLine;
            while (std::getline(file, currentLine)) {
                // Generate a Mesh Object or Prepare for an object to be created
                if (Algorithm::firstToken(currentLine) == "o" || Algorithm::firstToken(currentLine) == "g" ||
                    currentLine[0] == 'g') {
                    if (!listening) {
                        listening = true;

                        if (Algorithm::firstToken(currentLine) == "o" || Algorithm::firstToken(currentLine) == "g") {
                            meshName = Algorithm::tail(currentLine);
                        } else {
                            meshName = "unnamed";
                        }
                    } else {
                        // Generate the mesh to put into the array

                        if (!Indices.empty() && !Vertices.empty()) {
                            // Create Mesh
                            tempMesh = Mesh(Vertices, Indices);
                            tempMesh.MeshName = meshName;

                            // Insert Mesh
                            LoadedMeshes.push_back(tempMesh);

                            // Cleanup
                            Vertices.clear();
                            Indices.clear();
                            meshName.clear();

                            meshName = Algorithm::tail(currentLine);
                        } else {
                            if (Algorithm::firstToken(currentLine) == "o" || Algorithm::firstToken(currentLine) == "g") {
                                meshName = Algorithm::tail(currentLine);
                            } else {
                                meshName = "unnamed";
                            }
                        }
                    }
                }
                // Generate a Vertex Position
                if (Algorithm::firstToken(currentLine) == "v") {
                    std::vector<std::string> spos;
                    Vector3 vpos;
                    Algorithm::split(Algorithm::tail(currentLine), spos, " ");

                    vpos.X = std::stof(spos[0]);
                    vpos.Y = std::stof(spos[1]);
                    vpos.Z = std::stof(spos[2]);

                    Positions.push_back(vpos);
                }
                // Generate a Vertex Texture Coordinate
                if (Algorithm::firstToken(currentLine) == "vt") {
                    std::vector<std::string> stex;
                    Vector2 vtex;
                    Algorithm::split(Algorithm::tail(currentLine), stex, " ");

                    vtex.X = std::stof(stex[0]);
                    vtex.Y = std::stof(stex[1]);

                    TCoords.push_back(vtex);
                }
                // Generate a Vertex Normal;
                if (Algorithm::firstToken(currentLine) == "vn") {
                    std::vector<std::string> snor;
                    Vector3 vnor;
                    Algorithm::split(Algorithm::tail(currentLine), snor, " ");

                    vnor.X = std::stof(snor[0]);
                    vnor.Y = std::stof(snor[1]);
                    vnor.Z = std::stof(snor[2]);

                    Normals.push_back(vnor);
                }
                // Generate a Face (vertices & indices)
                if (Algorithm::firstToken(currentLine) == "f") {
                    // Generate the vertices
                    std::vector<Vertex> vVerts;
                    GenVerticesFromRawOBJ(vVerts, Positions, TCoords, Normals, currentLine);

                    // Add Vertices
                    for (const auto &vVert : vVerts) {
                        Vertices.push_back(vVert);
                        LoadedVertices.push_back(vVert);
                    }

                    std::vector<unsigned int> iIndices;

                    VertexTriangulation(iIndices, vVerts);

                    // Add Indices
                    for (unsigned int iIndice : iIndices) {
                        unsigned int indnum = (unsigned int) ((Vertices.size()) - vVerts.size()) + iIndice;
                        Indices.push_back(indnum);

                        indnum = (unsigned int) ((LoadedVertices.size()) - vVerts.size()) + iIndice;
                        LoadedIndices.push_back(indnum);

                    }
                }
                // Get Mesh Material Name
                if (Algorithm::firstToken(currentLine) == "usemtl") {
                    MeshMatNames.push_back(Algorithm::tail(currentLine));

                    // Create new Mesh, if Material changes within a group
                    if (!Indices.empty() && !Vertices.empty()) {
                        // Create Mesh
                        tempMesh = Mesh(Vertices, Indices);
                        tempMesh.MeshName = meshName;
                        int i = 2;
                        while (true) {
                            tempMesh.MeshName = meshName + "_" + std::to_string(i);

                            for (auto &m : LoadedMeshes) {
                                if (m.MeshName == tempMesh.MeshName) {
                                    continue;
                                }
                            }
                            break;
                        }

                        // Insert Mesh
                        LoadedMeshes.push_back(tempMesh);

                        // Cleanup
                        Vertices.clear();
                        Indices.clear();
                    }
                }
                // Load Materials
                if (Algorithm::firstToken(currentLine) == "mtllib") {
                    // Generate LoadedMaterial

                    // Generate a path to the material file
                    std::vector<std::string> temp;
                    Algorithm::split(Path, temp, "/");

                    std::string pathToMaterial;

                    if (temp.size() != 1) {
                        for (size_t i = 0; i < temp.size() - 1; i++) {
                            pathToMaterial += temp[i] + "/";
                        }
                    }
                    pathToMaterial += Algorithm::tail(currentLine);
                }
            }

            // Deal with last mesh

            if (!Indices.empty() && !Vertices.empty()) {
                // Create Mesh
                tempMesh = Mesh(Vertices, Indices);
                tempMesh.MeshName = meshName;

                // Insert Mesh
                LoadedMeshes.push_back(tempMesh);
            }
            file.close();
            return !(LoadedMeshes.empty() && LoadedVertices.empty() && LoadedIndices.empty());
        }

        // Loaded Mesh Objects
        std::vector<Mesh> LoadedMeshes;
        // Loaded Vertex Objects
        std::vector<Vertex> LoadedVertices;
        // Loaded Index Positions
        std::vector<unsigned int> LoadedIndices;
        // Loaded Material Objects
        //std::vector<Material> LoadedMaterials;

    private:
        // Generate vertices from a list of positions,
        //	T-coords, normals and a face line
        static void GenVerticesFromRawOBJ(std::vector<Vertex> &oVerts,
                                   const std::vector<Vector3> &iPositions,
                                   const std::vector<Vector2> &iTCoords,
                                   const std::vector<Vector3> &iNormals,
                                   const std::string &icurrentLine) {
            std::vector<std::string> sface, svert;
            Vertex vVert;
            Algorithm::split(Algorithm::tail(icurrentLine), sface, " ");

            bool noNormal = false;

            // For every given vertex do this
            for (const auto &i : sface) {
                // See What type the vertex is.
                int vtype(-1);

                Algorithm::split(i, svert, "/");

                // Check for just position - v1
                if (svert.size() == 1) {
                    // Only position
                    vtype = 1;
                }

                // Check for position & texture - v1/vt1
                if (svert.size() == 2) {
                    // Position & Texture
                    vtype = 2;
                }

                // Check for Position, Texture and Normal - v1/vt1/vn1
                // or if Position and Normal - v1//vn1
                if (svert.size() == 3) {
                    if (!svert[1].empty()) {
                        // Position, Texture, and Normal
                        vtype = 4;
                    } else {
                        // Position & Normal
                        vtype = 3;
                    }
                }

                // Calculate and store the vertex
                switch (vtype) {
                    case 1: // P
                    {
                        vVert.Position = Algorithm::getElement(iPositions, svert[0]);
                        vVert.TextureCoordinate = Vector2(0, 0);
                        noNormal = true;
                        oVerts.push_back(vVert);
                        break;
                    }
                    case 2: // P/T
                    {
                        vVert.Position = Algorithm::getElement(iPositions, svert[0]);
                        vVert.TextureCoordinate = Algorithm::getElement(iTCoords, svert[1]);
                        noNormal = true;
                        oVerts.push_back(vVert);
                        break;
                    }
                    case 3: // P//N
                    {
                        vVert.Position = Algorithm::getElement(iPositions, svert[0]);
                        vVert.TextureCoordinate = Vector2(0, 0);
                        vVert.Normal = Algorithm::getElement(iNormals, svert[2]);
                        oVerts.push_back(vVert);
                        break;
                    }
                    case 4: // P/T/N
                    {
                        vVert.Position = Algorithm::getElement(iPositions, svert[0]);
                        vVert.TextureCoordinate = Algorithm::getElement(iTCoords, svert[1]);
                        vVert.Normal = Algorithm::getElement(iNormals, svert[2]);
                        oVerts.push_back(vVert);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            // take care of missing normals
            // these may not be truly accurate but it is the
            // best they get for not compiling a mesh with normals
            if (noNormal) {
                Vector3 A = oVerts[0].Position - oVerts[1].Position;
                Vector3 B = oVerts[2].Position - oVerts[1].Position;

                Vector3 normal = Math::CrossVector3(A, B);

                for (auto &oVert : oVerts) {
                    oVert.Normal = normal;
                }
            }
        }

        //  Triangulate a list of vertices into a face by printing
        //	indices corresponding with triangles within it
        static void VertexTriangulation(std::vector<unsigned int> &oIndices,
                                 const std::vector<Vertex> &iVerts) {
            // If there are 2 or less verts,
            // no triangle can be created,
            // so exit
            if (iVerts.size() < 3) {
                return;
            }
            // If it is a triangle no need to calculate it
            if (iVerts.size() == 3) {
                oIndices.push_back(0);
                oIndices.push_back(1);
                oIndices.push_back(2);
                return;
            }

            // Create a list of vertices
            std::vector<Vertex> tVerts = iVerts;

            while (true) {
                // For every vertex
                for (int i = 0; i < int(tVerts.size()); i++) {
                    // pPrev = the previous vertex in the list
                    Vertex pPrev;
                    if (i == 0) {
                        pPrev = tVerts[tVerts.size() - 1];
                    } else {
                        pPrev = tVerts[i - 1];
                    }

                    // pCur = the current vertex;
                    Vertex pCur = tVerts[i];

                    // pNext = the next vertex in the list
                    Vertex pNext;
                    if (i == static_cast<int>(tVerts.size()) - 1) {
                        pNext = tVerts[0];
                    } else {
                        pNext = tVerts[i + 1];
                    }

                    // Check to see if there are only 3 verts left
                    // if so this is the last triangle
                    if (tVerts.size() == 3) {
                        // Create a triangle from pCur, pPrev, pNext
                        for (int j = 0; j < static_cast<int>(tVerts.size()); j++) {
                            if (iVerts[j].Position == pCur.Position) {
                                oIndices.push_back(static_cast<unsigned int &&>(j));
                            }
                            if (iVerts[j].Position == pPrev.Position) {
                                oIndices.push_back(static_cast<unsigned int &&>(j));
                            }
                            if (iVerts[j].Position == pNext.Position) {
                                oIndices.push_back(static_cast<unsigned int &&>(j));
                            }
                        }

                        tVerts.clear();
                        break;
                    }
                    if (tVerts.size() == 4) {
                        // Create a triangle from pCur, pPrev, pNext
                        for (int j = 0; j < static_cast<int>(iVerts.size()); j++) {
                            if (iVerts[j].Position == pCur.Position) {
                                oIndices.push_back(static_cast<unsigned int &&>(j));
                            }
                            if (iVerts[j].Position == pPrev.Position) {
                                oIndices.push_back(static_cast<unsigned int &&>(j));
                            }
                            if (iVerts[j].Position == pNext.Position) {
                                oIndices.push_back(static_cast<unsigned int &&>(j));
                            }
                        }

                        Vector3 tempVec;
                        for (auto &tVert : tVerts) {
                            if (tVert.Position != pCur.Position
                                && tVert.Position != pPrev.Position
                                && tVert.Position != pNext.Position) {
                                tempVec = tVert.Position;
                                break;
                            }
                        }

                        // Create a triangle from pCur, pPrev, pNext
                        for (int j = 0; j < static_cast<int>(iVerts.size()); j++) {
                            if (iVerts[j].Position == pPrev.Position) {
                                oIndices.push_back(static_cast<unsigned int &&>(j));
                            }
                            if (iVerts[j].Position == pNext.Position) {
                                oIndices.push_back(static_cast<unsigned int &&>(j));
                            }
                            if (iVerts[j].Position == tempVec) {
                                oIndices.push_back(static_cast<unsigned int &&>(j));
                            }
                        }

                        tVerts.clear();
                        break;
                    }

                    // If Vertex is not an interior vertex
                    auto angle =
                            static_cast<float>(Math::AngleBetweenVector3(pPrev.Position - pCur.Position,
                                                                         pNext.Position - pCur.Position) *
                                               (180 / M_PI));
                    if (angle <= 0 && angle >= 180) {
                        continue;
                    }

                    // If any vertices are within this triangle
                    bool withinTriangle = false;
                    for (const auto &iVert : iVerts) {
                        if (Algorithm::inTriangle(iVert.Position, pPrev.Position, pCur.Position, pNext.Position)
                            && iVert.Position != pPrev.Position
                            && iVert.Position != pCur.Position
                            && iVert.Position != pNext.Position) {
                            withinTriangle = true;
                            break;
                        }
                    }
                    if (withinTriangle) {
                        continue;
                    }

                    // Create a triangle from pCur, pPrev, pNext
                    for (int j = 0; j < static_cast<int>(iVerts.size()); j++) {
                        if (iVerts[j].Position == pCur.Position) {
                            oIndices.push_back(static_cast<unsigned int &&>(j));
                        }
                        if (iVerts[j].Position == pPrev.Position) {
                            oIndices.push_back(static_cast<unsigned int &&>(j));
                        }
                        if (iVerts[j].Position == pNext.Position) {
                            oIndices.push_back(static_cast<unsigned int &&>(j));
                        }
                    }

                    // Delete pCur from the list
                    for (int j = 0; j < static_cast<int>(tVerts.size()); j++) {
                        if (tVerts[j].Position == pCur.Position) {
                            tVerts.erase(tVerts.begin() + j);
                            break;
                        }
                    }

                    // reset i to the start
                    // -1 since loop will add 1 to it
                    i = -1;
                }

                // if no triangles were created
                if (oIndices.empty()) {
                    break;
                }
                // if no more vertices
                if (tVerts.empty()) {
                    break;
                }
            }
        }
    };
}
