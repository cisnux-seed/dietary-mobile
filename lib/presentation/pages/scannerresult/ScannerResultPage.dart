import 'dart:io';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

final class ScannerResultPage extends StatelessWidget {
  const ScannerResultPage({super.key, required this.foodPicture});

  final String? foodPicture;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Positioned.fill(
            child: SizedBox(
              height: double.infinity,
              width: double.infinity,
              child: foodPicture == null
                  ? const Placeholder()
                  : Image.file(
                      File(foodPicture!.substring(1, foodPicture!.length)),
                      fit: BoxFit.fill,
                    ),
            ),
          ),
          Positioned(
            left: 12,
            top: 36,
            child: CircleAvatar(
              backgroundColor: Colors.black.withOpacity(0.5),
              child: IconButton(
                onPressed: () => context.pop(),
                icon: const Icon(
                  Icons.arrow_back_rounded,
                  color: Colors.white,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
