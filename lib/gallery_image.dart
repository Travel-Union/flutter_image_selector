class GalleryImage {
  final String name;
  final String dateTaken;
  final String path;
  final int size;

  GalleryImage({this.name, this.dateTaken, this.path, this.size});

  static GalleryImage fromJson(Map<String, dynamic> c) {
    return GalleryImage(
      name: c["name"],
      dateTaken: c["dateTaken"],
      size: c["size"],
      path: c["path"],
    );
  }
}
