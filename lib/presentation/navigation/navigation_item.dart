import 'package:flutter/material.dart';
import 'package:freezed_annotation/freezed_annotation.dart';

part 'navigation_item.freezed.dart';

@freezed
abstract class NavigationItemIcon with _$NavigationItemIcon {
  const factory NavigationItemIcon({
    required String label,
    required IconData icon,
    required IconData selectedIcon,
  }) = _NavigationItemIcon;
}

@freezed
abstract class NavigationItemAsset with _$NavigationItemAsset {
  const factory NavigationItemAsset({
    required String label,
    required String asset,
  }) = _NavigationItemAsset;
}

final navigationItems = [
  const NavigationItemIcon(
    label: 'Home',
    icon: Icons.home_outlined,
    selectedIcon: Icons.home_rounded,
  ),
  const NavigationItemAsset(
    label: 'Report',
    asset: 'assets/images/ic_report_24dp.png',
  ),
  const NavigationItemIcon(
    label: 'My Profile',
    icon: Icons.account_circle_outlined,
    selectedIcon: Icons.account_circle_rounded,
  ),
];
