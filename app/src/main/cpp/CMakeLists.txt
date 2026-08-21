cmake_minimum_required(VERSION 3.4.1)
project(shadowpanel)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17 -O2 -fPIC")

add_library(shadowpanel SHARED
    native-lib.cpp
)

target_include_directories(shadowpanel PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
)

target_link_libraries(shadowpanel
    log
    pthread
)
