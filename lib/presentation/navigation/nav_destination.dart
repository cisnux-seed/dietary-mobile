enum NavDestination {
  home(path: '/'),
  report(path: '/report'),
  myProfile(path: '/myprofile'),
  foodScanner(path: 'foodscanner'),
  scannerResult(path: 'scannerresult');

  const NavDestination({required this.path});

  final String path;
}