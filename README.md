# Tetragon---2D-rectangle-mapping.
Library for mananging data that consists of non-overlaping axis aligned rectangles writen in java.

Making this library as i need it for a 2d voxel game :)

The main idea is to provide a set of operations as quickly as possible:
- Add rectangles (current peformance is O((logn)^2)) where n is the number of rectangles)
- Merge rectangles O(1)
- Remove a subrectangle from an existing rectangle O(1)
- Determine if a bounding rectangle overlaps any rectangle (current peformance is O((logn)^2) where n is the number of rectangles)
- get all rectangles in a given bounding rectangle (not implemented, but the peformance should be O((r log n)*(s log n)) where r and s is the number of paths down an interval tree for each dimension that have an overlap, and n is the number of rectangles)
- get intersection between two rectangles - O(1)

Planned functionality:
- get all touching rectangles
- get all networks of touching rectangles
- get all "sealed" rectangle networks (rectangles that are being touched around all of the circumfrence, or marked as a sealing rectangle)
- get rectangle belonging to point.

Main goal of the library is to be able to quickly describe a set of rectangles over network using minimal data.

