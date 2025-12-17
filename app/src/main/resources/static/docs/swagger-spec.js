window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "GrowIT API Specification",
    "description" : "GrowIT description",
    "version" : "0.0.3"
  },
  "servers" : [ {
    "url" : "https://dev.grow-it.me/"
  } ],
  "tags" : [ ],
  "paths" : {
    "/advice/grorong" : {
      "get" : {
        "tags" : [ "Advice" ],
        "summary" : "그로롱 조언 조회",
        "description" : "사용자 상태에 따른 그로롱의 격려 메시지와 명언을 조회합니다.",
        "operationId" : "get-grorong-advice",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/advice-grorong-1982709106"
                },
                "examples" : {
                  "get-grorong-advice" : {
                    "value" : "{\n  \"data\" : {\n    \"saying\" : \"오늘도 화이팅!\",\n    \"message\" : \"오다 주웠다\",\n    \"mood\" : \"HAPPY\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/advice/mentor" : {
      "get" : {
        "tags" : [ "Advice" ],
        "summary" : "멘토 조언 조회",
        "description" : "멘토 조언 조회",
        "operationId" : "get-mentor-advice",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/advice-mentor-1216775327"
                },
                "examples" : {
                  "get-mentor-advice" : {
                    "value" : "{\n  \"data\" : {\n    \"isChecked\" : true,\n    \"message\" : \"혁신은 단순함에서 시작돼\",\n    \"kpt\" : {\n      \"keep\" : \"투두 분해와 실행 리듬은 안정적이다.\",\n      \"problem\" : \"디자인 - 개발 QA 협업 속도가 더디다\",\n      \"tryNext\" : \"이번주는 MVP를 반드시 배포해라.\"\n    }\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/auth/reissue" : {
      "post" : {
        "tags" : [ "Auth" ],
        "summary" : "토큰 재발급",
        "description" : "토큰 재발급",
        "operationId" : "auth-reissue",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/auth-reissue356149288"
              },
              "examples" : {
                "auth-reissue" : {
                  "value" : "{\n  \"refreshToken\" : \"dummy-refresh-token\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/auth-signin-424105652"
                },
                "examples" : {
                  "auth-reissue" : {
                    "value" : "{\n  \"data\" : {\n    \"accessToken\" : \"accessToken\",\n    \"refreshToken\" : \"refreshToken\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/auth/signin" : {
      "post" : {
        "tags" : [ "Auth" ],
        "summary" : "사용자 로그인",
        "description" : "사용자 로그인",
        "operationId" : "auth-signin",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/auth-signin32710318"
              },
              "examples" : {
                "auth-signin" : {
                  "value" : "{\n  \"email\" : \"test@example.com\",\n  \"password\" : \"securePass123\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/auth-signin-424105652"
                },
                "examples" : {
                  "auth-signin" : {
                    "value" : "{\n  \"data\" : {\n    \"accessToken\" : \"accessToken\",\n    \"refreshToken\" : \"refreshToken\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/auth/signup" : {
      "post" : {
        "tags" : [ "Auth" ],
        "summary" : "사용자 회원가입",
        "description" : "사용자 회원가입",
        "operationId" : "auth-signup",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/auth-signup-1821282286"
              },
              "examples" : {
                "auth-signup" : {
                  "value" : "{\n  \"email\" : \"test@example.com\",\n  \"password\" : \"securePass123\",\n  \"name\" : \"홍길동\",\n  \"jobRoleId\" : \"6rOg7Zmp7IOd\",\n  \"careerYear\" : \"JUNIOR\",\n  \"requiredConsent\" : {\n    \"privacyPolicyAgreed\" : true,\n    \"serviceTermsAgreed\" : true\n  }\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "201" : {
            "description" : "201"
          }
        }
      }
    },
    "/auth/signup/kakao" : {
      "post" : {
        "tags" : [ "Auth" ],
        "summary" : "카카오 회원가입",
        "description" : "카카오 회원가입",
        "operationId" : "auth-signup-kakao",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/auth-signup-kakao-290542722"
              },
              "examples" : {
                "auth-signup-kakao" : {
                  "value" : "{\n  \"name\" : \"홍길동\",\n  \"jobRoleId\" : \"6rOg7Zmp7IOd\",\n  \"careerYear\" : \"JUNIOR\",\n  \"requiredConsent\" : {\n    \"privacyPolicyAgreed\" : true,\n    \"serviceTermsAgreed\" : true\n  },\n  \"registrationToken\" : \"dummy-registration-token\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "201" : {
            "description" : "201"
          }
        }
      }
    },
    "/externals/invitations" : {
      "post" : {
        "tags" : [ "Invitation" ],
        "summary" : "초대장 요청 생성",
        "description" : "초대장 요청 생성",
        "operationId" : "create-invitation",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/externals-invitations874860244"
              },
              "examples" : {
                "create-invitation" : {
                  "value" : "{\n  \"phone\" : \"010-1234-5678\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/externals-invitations83146364"
                },
                "examples" : {
                  "create-invitation" : {
                    "value" : "{\n  \"data\" : {\n    \"message\" : \"초대장 요청이 전송되었습니다.\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/goal-retrospects" : {
      "get" : {
        "tags" : [ "Goal Retrospects" ],
        "summary" : "연도별 목표+회고 목록 조회",
        "description" : "연도별 목표+회고 목록 조회",
        "operationId" : "get-goal-retrospects-by-year",
        "parameters" : [ {
          "name" : "year",
          "in" : "query",
          "description" : "조회 연도 (예: 2025)",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goal-retrospects1943232045"
                },
                "examples" : {
                  "get-goal-retrospects-by-year" : {
                    "value" : "{\n  \"data\" : [ {\n    \"goal\" : {\n      \"id\" : \"goal-1\",\n      \"name\" : \"테스트 목표\",\n      \"duration\" : {\n        \"startDate\" : \"2025-12-15\",\n        \"endDate\" : \"2025-12-21\"\n      }\n    },\n    \"goalRetrospect\" : {\n      \"id\" : \"WayydeZetlLHHm2hZ_3IC\",\n      \"isCompleted\" : true\n    }\n  } ]\n}"
                  }
                }
              }
            }
          }
        }
      },
      "post" : {
        "tags" : [ "Goal Retrospects" ],
        "summary" : "목표 회고 생성",
        "description" : "목표 회고 생성",
        "operationId" : "create-goal-retrospect",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goal-retrospects-942583737"
              },
              "examples" : {
                "create-goal-retrospect" : {
                  "value" : "{\n  \"goalId\" : \"goalId\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "201" : {
            "description" : "201",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goal-retrospects-1650437255"
                },
                "examples" : {
                  "create-goal-retrospect" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"id\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/goal-retrospects/{id}" : {
      "get" : {
        "tags" : [ "Goal Retrospects" ],
        "summary" : "목표회고 단건 조회",
        "description" : "목표회고 단건 조회",
        "operationId" : "get-goal-retrospect",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "목표회고 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goal-retrospects-id707367061"
                },
                "examples" : {
                  "get-goal-retrospect" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"x8Y6SO2C9q25LjmjfKaKf\",\n    \"goalId\" : \"goalId\",\n    \"todoCompletedRate\" : 25,\n    \"analysis\" : {\n      \"summary\" : \"GROWIT MVP 개발과 서비스 기획을 병행하며 4주 목표를 달성\",\n      \"advice\" : \"모든 활동이 한 가지 핵심 가치에 연결되도록 중심축을 명확히 해보라냥!\"\n    },\n    \"content\" : \"이번 달 나는 '나만의 의미 있는 일'을 찾기 위해 다양한 프로젝트와 리서치에 몰입했다...\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      },
      "patch" : {
        "tags" : [ "Goal Retrospects" ],
        "summary" : "목표 회고 수정",
        "description" : "목표 회고 수정",
        "operationId" : "update-goal-retrospect",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "목표 회고 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goal-retrospects-id130578538"
              },
              "examples" : {
                "update-goal-retrospect" : {
                  "value" : "{\n  \"content\" : \"내 목표는 그로잇 완성\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200"
          }
        }
      }
    },
    "/goals" : {
      "get" : {
        "tags" : [ "Goals" ],
        "summary" : "내 목표 목록 조회",
        "description" : "사용자의 목표 목록을 상태별로 조회합니다.",
        "operationId" : "get-my-goals",
        "parameters" : [ {
          "name" : "status",
          "in" : "query",
          "description" : "목표 상태 (PROGRESS, ENDED, COMPLETED)",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals-72449656"
                },
                "examples" : {
                  "get-my-goals" : {
                    "value" : "{\n  \"data\" : [ {\n    \"id\" : \"goal-1\",\n    \"name\" : \"테스트 목표\",\n    \"planet\" : {\n      \"name\" : \"Earth\",\n      \"image\" : {\n        \"done\" : \"/images/earth_done.png\",\n        \"progress\" : \"/images/earth_progress.png\"\n      }\n    },\n    \"duration\" : {\n      \"startDate\" : \"2025-12-15\",\n      \"endDate\" : \"2025-12-21\"\n    },\n    \"status\" : \"PROGRESS\",\n    \"analysis\" : {\n      \"todoCompletedRate\" : 75,\n      \"summary\" : \"목표가 순조롭게 진행되고 있습니다.\"\n    },\n    \"isChecked\" : false\n  } ]\n}"
                  }
                }
              }
            }
          }
        }
      },
      "post" : {
        "tags" : [ "Goals" ],
        "summary" : "목표 생성",
        "description" : "새로운 목표를 생성합니다.",
        "operationId" : "create-goal",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goals-1576692806"
              },
              "examples" : {
                "create-goal" : {
                  "value" : "{\n  \"name\" : \"내 목표는 그로잇 완성\",\n  \"duration\" : {\n    \"startDate\" : \"2025-12-22\",\n    \"endDate\" : \"2026-01-18\"\n  }\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "201" : {
            "description" : "201",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals-63574136"
                },
                "examples" : {
                  "create-goal" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"goal-1\",\n    \"planet\" : {\n      \"name\" : \"Earth\",\n      \"image\" : {\n        \"done\" : \"/images/earth_done.png\",\n        \"progress\" : \"/images/earth_progress.png\"\n      }\n    }\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/goals/{id}" : {
      "get" : {
        "tags" : [ "Goals" ],
        "summary" : "특정 목표 조회",
        "description" : "특정 목표의 상세 정보를 조회합니다.",
        "operationId" : "get-my-goal",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "목표 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals-id-926231842"
                },
                "examples" : {
                  "get-my-goal" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"goal-1\",\n    \"name\" : \"테스트 목표\",\n    \"planet\" : {\n      \"name\" : \"Earth\",\n      \"image\" : {\n        \"done\" : \"/images/earth_done.png\",\n        \"progress\" : \"/images/earth_progress.png\"\n      }\n    },\n    \"duration\" : {\n      \"startDate\" : \"2025-12-15\",\n      \"endDate\" : \"2025-12-21\"\n    },\n    \"status\" : \"PROGRESS\",\n    \"analysis\" : {\n      \"todoCompletedRate\" : 75,\n      \"summary\" : \"목표가 순조롭게 진행되고 있습니다.\"\n    },\n    \"isChecked\" : false\n  }\n}"
                  }
                }
              }
            }
          }
        }
      },
      "put" : {
        "tags" : [ "Goals" ],
        "summary" : "목표 수정",
        "description" : "기존 목표 정보를 수정합니다.",
        "operationId" : "update-goal",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "목표 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goals-id529992684"
              },
              "examples" : {
                "update-goal" : {
                  "value" : "{\n  \"name\" : \"내 목표는 그로잇 완성\",\n  \"duration\" : {\n    \"startDate\" : \"2025-12-22\",\n    \"endDate\" : \"2026-01-18\"\n  }\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals-id233228607"
                },
                "examples" : {
                  "update-goal" : {
                    "value" : "{\n  \"data\" : \"목표가 수정 완료되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      },
      "delete" : {
        "tags" : [ "Goals" ],
        "summary" : "목표 삭제",
        "description" : "특정 목표를 삭제합니다.",
        "operationId" : "delete-goal",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "삭제할 목표 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals-id-393629335"
                },
                "examples" : {
                  "delete-goal" : {
                    "value" : "{\n  \"data\" : \"삭제되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/resource/jobroles" : {
      "get" : {
        "tags" : [ "JobRole" ],
        "summary" : "전체 직무 목록 조회",
        "description" : "전체 직무 목록 조회",
        "operationId" : "get-job-roles",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/resource-jobroles118231371"
                },
                "examples" : {
                  "get-job-roles" : {
                    "value" : "{\n  \"data\" : {\n    \"jobRoles\" : [ {\n      \"id\" : \"dev\",\n      \"name\" : \"개발자\"\n    }, {\n      \"id\" : \"designer\",\n      \"name\" : \"디자이너\"\n    }, {\n      \"id\" : \"planner\",\n      \"name\" : \"기획자\"\n    } ]\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/resource/saying" : {
      "get" : {
        "tags" : [ "Saying" ],
        "summary" : "격언 조회",
        "description" : "격언 조회",
        "operationId" : "get-saying",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/resource-saying2030450560"
                },
                "examples" : {
                  "get-saying" : {
                    "value" : "{\n  \"data\" : {\n    \"message\" : \"성공은 매일 반복되는 작은 노력들의 합이다냥!\",\n    \"from\" : \"그로냥\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/todos" : {
      "get" : {
        "tags" : [ "Todos" ],
        "summary" : "날짜별 ToDo 조회",
        "description" : "특정 날짜의 ToDo 목록을 목표 정보와 함께 조회합니다.",
        "operationId" : "get-todo",
        "parameters" : [ {
          "name" : "date",
          "in" : "query",
          "description" : "조회할 날짜 (yyyy-MM-dd)",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "from",
          "in" : "query",
          "description" : "시작 날짜 (yyyy-MM-dd)",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "to",
          "in" : "query",
          "description" : "종료 날짜 (yyyy-MM-dd)",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos1823093792"
                },
                "examples" : {
                  "get-todos-by-date" : {
                    "value" : "{\n  \"data\" : [ {\n    \"todo\" : {\n      \"id\" : \"todo-1\",\n      \"goalId\" : \"goal-1\",\n      \"date\" : \"2024-01-01\",\n      \"content\" : \"테스트 할 일입니다.\",\n      \"isImportant\" : false,\n      \"isCompleted\" : false,\n      \"routine\" : null\n    },\n    \"goal\" : {\n      \"id\" : \"goal-1\",\n      \"name\" : \"테스트 목표\"\n    }\n  }, {\n    \"todo\" : {\n      \"id\" : \"todo-2\",\n      \"goalId\" : \"goal-1\",\n      \"date\" : \"2024-01-01\",\n      \"content\" : \"테스트 할 일입니다.\",\n      \"isImportant\" : false,\n      \"isCompleted\" : false,\n      \"routine\" : null\n    },\n    \"goal\" : {\n      \"id\" : \"goal-1\",\n      \"name\" : \"테스트 목표\"\n    }\n  } ]\n}"
                  },
                  "get-todo-count-by-date-range" : {
                    "value" : "{\n  \"data\" : [ {\n    \"date\" : \"2024-01-01\",\n    \"goals\" : [ {\n      \"id\" : \"goal-1\",\n      \"todoCount\" : 3\n    }, {\n      \"id\" : \"goal-2\",\n      \"todoCount\" : 2\n    } ]\n  }, {\n    \"date\" : \"2024-01-02\",\n    \"goals\" : [ {\n      \"id\" : \"goal-1\",\n      \"todoCount\" : 1\n    }, {\n      \"id\" : \"goal-2\",\n      \"todoCount\" : 4\n    } ]\n  } ]\n}"
                  }
                }
              }
            }
          }
        }
      },
      "post" : {
        "tags" : [ "Todos" ],
        "summary" : "ToDo 생성",
        "description" : "새로운 ToDo를 생성합니다.",
        "operationId" : "create-todo",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/todos-620286235"
              },
              "examples" : {
                "create-todo" : {
                  "value" : "{\n  \"goalId\" : \"goal-1\",\n  \"date\" : \"2025-12-17\",\n  \"content\" : \"할 일 예시 내용입니다.\",\n  \"isImportant\" : false,\n  \"routine\" : null\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "201" : {
            "description" : "201",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos1307619782"
                },
                "examples" : {
                  "create-todo" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"todo-1\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/todos/{id}" : {
      "get" : {
        "tags" : [ "Todos" ],
        "summary" : "특정 ToDo 조회",
        "description" : "특정 ToDo의 상세 정보를 조회합니다.",
        "operationId" : "get-todo-by-id",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "ToDo ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-id-1315705963"
                },
                "examples" : {
                  "get-todo-by-id" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"todo-1\",\n    \"goalId\" : \"goal-1\",\n    \"content\" : \"테스트 할 일입니다.\",\n    \"date\" : \"2025-12-17\",\n    \"isCompleted\" : false,\n    \"isImportant\" : false,\n    \"routine\" : null\n  }\n}"
                  }
                }
              }
            }
          }
        }
      },
      "put" : {
        "tags" : [ "Todos" ],
        "summary" : "ToDo 수정",
        "description" : "기존 ToDo 정보를 수정합니다.",
        "operationId" : "update-todo",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "ToDo ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/todos-id-1443446372"
              },
              "examples" : {
                "update-todo" : {
                  "value" : "{\n  \"date\" : \"2025-12-17\",\n  \"content\" : \"수정된 할 일 내용\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200"
          }
        }
      },
      "delete" : {
        "tags" : [ "Todos" ],
        "summary" : "ToDo 삭제",
        "description" : "특정 ToDo를 삭제합니다.",
        "operationId" : "delete-todo",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "삭제할 ToDo ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals-id-393629335"
                },
                "examples" : {
                  "delete-todo" : {
                    "value" : "{\n  \"data\" : \"삭제가 완료되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      },
      "patch" : {
        "tags" : [ "Todos" ],
        "summary" : "ToDo 상태 변경",
        "description" : "ToDo의 완료 상태나 중요도를 변경합니다.",
        "operationId" : "change-todo-status",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "ToDo ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/todos-id-925192925"
              },
              "examples" : {
                "change-todo-status" : {
                  "value" : "{\n  \"isCompleted\" : null,\n  \"isImportant\" : null\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-id1726757658"
                },
                "examples" : {
                  "change-todo-status" : {
                    "value" : "{\n  \"data\" : \"상태 변경이 완료되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/users/myprofile" : {
      "get" : {
        "tags" : [ "User" ],
        "summary" : "사용자 조회",
        "description" : "사용자 조회",
        "operationId" : "get-user",
        "parameters" : [ {
          "name" : "Authorization",
          "in" : "header",
          "description" : "JWT (Your Token)",
          "required" : true,
          "schema" : {
            "type" : "string"
          },
          "example" : "Bearer mock-jwt-token"
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/users-myprofile638373110"
                },
                "examples" : {
                  "get-user" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"user-1\",\n    \"email\" : \"user@example.com\",\n    \"name\" : \"testUser\",\n    \"jobRole\" : {\n      \"id\" : \"dev\",\n      \"name\" : \"개발자\"\n    },\n    \"careerYear\" : \"JUNIOR\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      },
      "put" : {
        "tags" : [ "User" ],
        "summary" : "사용자 업데이트",
        "description" : "사용자 업데이트",
        "operationId" : "update-user",
        "parameters" : [ {
          "name" : "Authorization",
          "in" : "header",
          "description" : "JWT (Your Token)",
          "required" : true,
          "schema" : {
            "type" : "string"
          },
          "example" : "Bearer mock-jwt-token"
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/users-myprofile-662092072"
              },
              "examples" : {
                "update-user" : {
                  "value" : "{\n  \"name\" : \"updatedName\",\n  \"jobRoleId\" : \"jobRoleId-1\",\n  \"careerYear\" : \"JUNIOR\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/users-myprofile-995496151"
                },
                "examples" : {
                  "update-user" : {
                    "value" : "{\n  \"data\" : \"사용자 정보를 업데이트 하였습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      },
      "delete" : {
        "tags" : [ "User" ],
        "summary" : "사용자 탈퇴",
        "description" : "사용자 탈퇴",
        "operationId" : "delete-user",
        "parameters" : [ {
          "name" : "Authorization",
          "in" : "header",
          "description" : "JWT (Your Token)",
          "required" : true,
          "schema" : {
            "type" : "string"
          },
          "example" : "Bearer mock-jwt-token"
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/users-myprofile-1476599188"
                },
                "examples" : {
                  "delete-user" : {
                    "value" : "{\n  \"data\" : \"탈퇴 처리 되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/users/myprofile/logout" : {
      "post" : {
        "tags" : [ "User" ],
        "summary" : "사용자 로그아웃",
        "description" : "사용자 로그아웃",
        "operationId" : "logout-user",
        "parameters" : [ {
          "name" : "Authorization",
          "in" : "header",
          "description" : "JWT (Your Token)",
          "required" : true,
          "schema" : {
            "type" : "string"
          },
          "example" : "Bearer mock-jwt-token"
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/users-myprofile-logout1299858205"
                },
                "examples" : {
                  "logout-user" : {
                    "value" : "{\n  \"data\" : \"로그아웃 되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/users/myprofile/onboarding" : {
      "get" : {
        "tags" : [ "User" ],
        "summary" : "온보딩 여부 조회",
        "description" : "온보딩 여부 조회",
        "operationId" : "get-user-onboarding",
        "parameters" : [ {
          "name" : "Authorization",
          "in" : "header",
          "description" : "JWT (Your Token)",
          "required" : true,
          "schema" : {
            "type" : "string"
          },
          "example" : "Bearer mock-jwt-token"
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/users-myprofile-onboarding1739902847"
                },
                "examples" : {
                  "get-user-onboarding" : {
                    "value" : "{\n  \"data\" : false\n}"
                  }
                }
              }
            }
          }
        }
      },
      "put" : {
        "tags" : [ "User" ],
        "summary" : "온보딩 완료 처리",
        "description" : "온보딩 완료 처리",
        "operationId" : "complete-user-onboarding",
        "parameters" : [ {
          "name" : "Authorization",
          "in" : "header",
          "description" : "JWT (Your Token)",
          "required" : true,
          "schema" : {
            "type" : "string"
          },
          "example" : "Bearer mock-jwt-token"
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/users-myprofile-onboarding-878420979"
                },
                "examples" : {
                  "complete-user-onboarding" : {
                    "value" : "{\n  \"data\" : \"온보딩 완료하였습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/users/myprofile/promotion" : {
      "post" : {
        "tags" : [ "User" ],
        "summary" : "프로모션 등록",
        "description" : "프로모션 등록",
        "operationId" : "register-promotion",
        "parameters" : [ {
          "name" : "Authorization",
          "in" : "header",
          "description" : "JWT (Your Token)",
          "required" : true,
          "schema" : {
            "type" : "string"
          },
          "example" : "Bearer mock-jwt-token"
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/users-myprofile-promotion-954185825"
              },
              "examples" : {
                "register-promotion" : {
                  "value" : "{\n  \"code\" : \"PROMO2024\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/users-myprofile-promotion506539620"
                },
                "examples" : {
                  "register-promotion" : {
                    "value" : "{\n  \"data\" : \"등록되었습니다\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    }
  },
  "components" : {
    "schemas" : {
      "users-myprofile-995496151" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "업데이트 성공 메세지"
          }
        }
      },
      "resource-saying2030450560" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "from" : {
                "type" : "string",
                "description" : "격언 출처"
              },
              "message" : {
                "type" : "string",
                "description" : "격언 내용"
              }
            }
          }
        }
      },
      "todos-id-1315705963" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "date" : {
                "type" : "string",
                "description" : "ToDo 날짜"
              },
              "routine" : {
                "type" : "object",
                "description" : "루틴 정보 (null 가능)"
              },
              "goalId" : {
                "type" : "string",
                "description" : "목표 ID"
              },
              "isImportant" : {
                "type" : "boolean",
                "description" : "중요도 여부"
              },
              "id" : {
                "type" : "string",
                "description" : "ToDo ID"
              },
              "content" : {
                "type" : "string",
                "description" : "ToDo 내용"
              },
              "isCompleted" : {
                "type" : "boolean",
                "description" : "완료 여부"
              }
            },
            "description" : "ToDo 상세 정보"
          }
        }
      },
      "resource-jobroles118231371" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "jobRoles" : {
                "type" : "array",
                "items" : {
                  "type" : "object",
                  "properties" : {
                    "name" : {
                      "type" : "string",
                      "description" : "직무 이름"
                    },
                    "id" : {
                      "type" : "string",
                      "description" : "직무 ID"
                    }
                  }
                }
              }
            }
          }
        }
      },
      "externals-invitations83146364" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "message" : {
                "type" : "string",
                "description" : "응답 메시지"
              }
            }
          }
        }
      },
      "todos1823093792" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "array",
            "description" : "날짜별 ToDo 개수 데이터 배열",
            "items" : {
              "type" : "object",
              "properties" : {
                "date" : {
                  "type" : "string",
                  "description" : "날짜 (yyyy-MM-dd)"
                },
                "todo" : {
                  "type" : "object",
                  "properties" : {
                    "date" : {
                      "type" : "string",
                      "description" : "ToDo 날짜"
                    },
                    "routine" : {
                      "type" : "object",
                      "description" : "루틴 정보 (null 가능)"
                    },
                    "goalId" : {
                      "type" : "string",
                      "description" : "목표 ID"
                    },
                    "isImportant" : {
                      "type" : "boolean",
                      "description" : "중요도 여부"
                    },
                    "id" : {
                      "type" : "string",
                      "description" : "ToDo ID"
                    },
                    "content" : {
                      "type" : "string",
                      "description" : "ToDo 내용"
                    },
                    "isCompleted" : {
                      "type" : "boolean",
                      "description" : "완료 여부"
                    }
                  },
                  "description" : "ToDo 정보"
                },
                "goal" : {
                  "type" : "object",
                  "properties" : {
                    "name" : {
                      "type" : "string",
                      "description" : "목표 이름"
                    },
                    "id" : {
                      "type" : "string",
                      "description" : "목표 ID"
                    }
                  },
                  "description" : "목표 정보"
                },
                "goals" : {
                  "type" : "array",
                  "description" : "목표별 ToDo 개수 배열",
                  "items" : {
                    "type" : "object",
                    "properties" : {
                      "todoCount" : {
                        "type" : "number",
                        "description" : "해당 목표의 ToDo 개수"
                      },
                      "id" : {
                        "type" : "string",
                        "description" : "목표 ID"
                      }
                    }
                  }
                }
              }
            }
          }
        }
      },
      "users-myprofile-onboarding1739902847" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "boolean",
            "description" : "온보딩 진행 여부"
          }
        }
      },
      "auth-signup-kakao-290542722" : {
        "type" : "object",
        "properties" : {
          "registrationToken" : {
            "type" : "string",
            "description" : "카카오 등록 토큰"
          },
          "careerYear" : {
            "type" : "string",
            "description" : "경력 연차 (예: JUNIOR, MID, SENIOR)"
          },
          "name" : {
            "type" : "string",
            "description" : "사용자 이름"
          },
          "requiredConsent" : {
            "type" : "object",
            "properties" : {
              "serviceTermsAgreed" : {
                "type" : "boolean",
                "description" : "서비스 약관 동의"
              },
              "privacyPolicyAgreed" : {
                "type" : "boolean",
                "description" : "개인정보 동의"
              }
            }
          },
          "jobRoleId" : {
            "type" : "string",
            "description" : "직무 ID"
          }
        }
      },
      "users-myprofile-1476599188" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "탈퇴 성공 메세지"
          }
        }
      },
      "users-myprofile-662092072" : {
        "type" : "object",
        "properties" : {
          "careerYear" : {
            "type" : "string",
            "description" : "경력 연차"
          },
          "name" : {
            "type" : "string",
            "description" : "이름"
          },
          "jobRoleId" : {
            "type" : "string",
            "description" : "직무 ID"
          }
        }
      },
      "auth-reissue356149288" : {
        "type" : "object",
        "properties" : {
          "refreshToken" : {
            "type" : "string",
            "description" : "리프레시 토큰"
          }
        }
      },
      "goals-id-926231842" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "duration" : {
                "type" : "object",
                "properties" : {
                  "endDate" : {
                    "type" : "string",
                    "description" : "종료 날짜 (yyyy-MM-dd)"
                  },
                  "startDate" : {
                    "type" : "string",
                    "description" : "시작 날짜 (yyyy-MM-dd)"
                  }
                },
                "description" : "목표 기간 정보"
              },
              "planet" : {
                "type" : "object",
                "properties" : {
                  "image" : {
                    "type" : "object",
                    "properties" : {
                      "progress" : {
                        "type" : "string",
                        "description" : "진행 이미지 URL"
                      },
                      "done" : {
                        "type" : "string",
                        "description" : "완료 이미지 URL"
                      }
                    },
                    "description" : "행성 이미지 정보"
                  },
                  "name" : {
                    "type" : "string",
                    "description" : "행성 이름"
                  }
                },
                "description" : "행성 정보"
              },
              "name" : {
                "type" : "string",
                "description" : "목표 이름"
              },
              "id" : {
                "type" : "string",
                "description" : "목표 ID"
              },
              "analysis" : {
                "type" : "object",
                "properties" : {
                  "summary" : {
                    "type" : "string",
                    "description" : "분석 요약 메시지"
                  },
                  "todoCompletedRate" : {
                    "type" : "number",
                    "description" : "ToDo 완료율 (0-100)"
                  }
                },
                "description" : "목표 분석 정보 (null 가능)"
              },
              "isChecked" : {
                "type" : "boolean",
                "description" : "목표 확인 여부"
              },
              "status" : {
                "type" : "string",
                "description" : "목표 상태 (PROGRESS, ENDED)"
              }
            },
            "description" : "목표 상세 정보"
          }
        }
      },
      "users-myprofile-promotion506539620" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "프로모션 등록 성공 메세지"
          }
        }
      },
      "goal-retrospects-942583737" : {
        "type" : "object",
        "properties" : {
          "goalId" : {
            "type" : "string",
            "description" : "목표 아이디"
          }
        }
      },
      "advice-mentor-1216775327" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "message" : {
                "type" : "string",
                "description" : "멘토 조언 메시지"
              },
              "isChecked" : {
                "type" : "boolean",
                "description" : "조언 확인 여부"
              },
              "kpt" : {
                "type" : "object",
                "properties" : {
                  "problem" : {
                    "type" : "string",
                    "description" : "Problem - 문제점"
                  },
                  "tryNext" : {
                    "type" : "string",
                    "description" : "Try - 시도할 것"
                  },
                  "keep" : {
                    "type" : "string",
                    "description" : "Keep - 계속할 것"
                  }
                },
                "description" : "KPT 피드백 객체"
              }
            }
          }
        }
      },
      "goals-id233228607" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "수정 완료 메시지"
          }
        }
      },
      "goal-retrospects-1650437255" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "id" : {
                "type" : "string",
                "description" : "목표 회고 아이디"
              }
            }
          }
        }
      },
      "advice-grorong-1982709106" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "mood" : {
                "type" : "string",
                "description" : "현재 기분 상태 (HAPPY, NORMAL, SAD)"
              },
              "saying" : {
                "type" : "string",
                "description" : "그로롱이 전하는 명언"
              },
              "message" : {
                "type" : "string",
                "description" : "현재 기분에 따른 격려 메시지"
              }
            }
          }
        }
      },
      "auth-signin32710318" : {
        "type" : "object",
        "properties" : {
          "password" : {
            "type" : "string",
            "description" : "사용자 비밀번호"
          },
          "email" : {
            "type" : "string",
            "description" : "사용자 이메일"
          }
        }
      },
      "goals-id-393629335" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "삭제 완료 메시지"
          }
        }
      },
      "goal-retrospects-id130578538" : {
        "type" : "object",
        "properties" : {
          "content" : {
            "type" : "string",
            "description" : "회고 내용"
          }
        }
      },
      "todos-620286235" : {
        "type" : "object",
        "properties" : {
          "date" : {
            "type" : "string",
            "description" : "ToDo 날짜 (yyyy-MM-dd)"
          },
          "routine" : {
            "type" : "object",
            "description" : "루틴 정보 (선택사항)"
          },
          "goalId" : {
            "type" : "string",
            "description" : "목표 ID"
          },
          "isImportant" : {
            "type" : "boolean",
            "description" : "중요도 여부"
          },
          "content" : {
            "type" : "string",
            "description" : "ToDo 내용 (1-30자)"
          }
        }
      },
      "auth-signup-1821282286" : {
        "type" : "object",
        "properties" : {
          "careerYear" : {
            "type" : "string",
            "description" : "경력 연차 (예: JUNIOR, MID, SENIOR)"
          },
          "password" : {
            "type" : "string",
            "description" : "사용자 비밀번호"
          },
          "name" : {
            "type" : "string",
            "description" : "사용자 이름"
          },
          "requiredConsent" : {
            "type" : "object",
            "properties" : {
              "serviceTermsAgreed" : {
                "type" : "boolean",
                "description" : "서비스 약관 동의"
              },
              "privacyPolicyAgreed" : {
                "type" : "boolean",
                "description" : "개인정보 동의"
              }
            }
          },
          "jobRoleId" : {
            "type" : "string",
            "description" : "직무 ID"
          },
          "email" : {
            "type" : "string",
            "description" : "사용자 이메일"
          }
        }
      },
      "goals-72449656" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "array",
            "description" : "목록 데이터 배열",
            "items" : {
              "type" : "object",
              "properties" : {
                "duration" : {
                  "type" : "object",
                  "properties" : {
                    "endDate" : {
                      "type" : "string",
                      "description" : "종료 날짜 (yyyy-MM-dd)"
                    },
                    "startDate" : {
                      "type" : "string",
                      "description" : "시작 날짜 (yyyy-MM-dd)"
                    }
                  },
                  "description" : "목표 기간 정보"
                },
                "planet" : {
                  "type" : "object",
                  "properties" : {
                    "image" : {
                      "type" : "object",
                      "properties" : {
                        "progress" : {
                          "type" : "string",
                          "description" : "진행 이미지 URL"
                        },
                        "done" : {
                          "type" : "string",
                          "description" : "완료 이미지 URL"
                        }
                      },
                      "description" : "행성 이미지 정보"
                    },
                    "name" : {
                      "type" : "string",
                      "description" : "행성 이름"
                    }
                  },
                  "description" : "행성 정보"
                },
                "name" : {
                  "type" : "string",
                  "description" : "목표 이름"
                },
                "id" : {
                  "type" : "string",
                  "description" : "목표 ID"
                },
                "analysis" : {
                  "type" : "object",
                  "properties" : {
                    "summary" : {
                      "type" : "string",
                      "description" : "분석 요약 메시지"
                    },
                    "todoCompletedRate" : {
                      "type" : "number",
                      "description" : "ToDo 완료율 (0-100)"
                    }
                  },
                  "description" : "목표 분석 정보 (null 가능)"
                },
                "isChecked" : {
                  "type" : "boolean",
                  "description" : "목표 확인 여부"
                },
                "status" : {
                  "type" : "string",
                  "description" : "목표 상태 (PROGRESS, ENDED)"
                }
              }
            }
          }
        }
      },
      "users-myprofile-logout1299858205" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "로그아웃 성공 메세지"
          }
        }
      },
      "users-myprofile-onboarding-878420979" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "온보딩 완료 성공 메세지"
          }
        }
      },
      "todos-id-1443446372" : {
        "type" : "object",
        "properties" : {
          "date" : {
            "type" : "string",
            "description" : "수정할 ToDo 날짜 (yyyy-MM-dd)"
          },
          "content" : {
            "type" : "string",
            "description" : "수정할 ToDo 내용 (1-30자)"
          }
        }
      },
      "externals-invitations874860244" : {
        "type" : "object",
        "properties" : {
          "phone" : {
            "type" : "string",
            "description" : "전화번호 (010-1234-5678 형식)"
          }
        }
      },
      "auth-signin-424105652" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "accessToken" : {
                "type" : "string",
                "description" : "엑세스 토큰"
              },
              "refreshToken" : {
                "type" : "string",
                "description" : "리프레시 토큰"
              }
            }
          }
        }
      },
      "todos1307619782" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "id" : {
                "type" : "string",
                "description" : "생성된 ToDo ID"
              }
            },
            "description" : "생성된 ToDo 정보"
          }
        }
      },
      "todos-id1726757658" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "상태 변경 완료 메시지"
          }
        }
      },
      "todos-id-925192925" : {
        "type" : "object",
        "properties" : {
          "isImportant" : {
            "type" : "boolean",
            "description" : "중요도 여부 (선택사항)"
          },
          "isCompleted" : {
            "type" : "boolean",
            "description" : "완료 여부 (선택사항)"
          }
        }
      },
      "users-myprofile638373110" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "careerYear" : {
                "type" : "string",
                "description" : "경력 연차"
              },
              "name" : {
                "type" : "string",
                "description" : "이름"
              },
              "jobRole" : {
                "type" : "object",
                "properties" : {
                  "name" : {
                    "type" : "string",
                    "description" : "직무 이름"
                  },
                  "id" : {
                    "type" : "string",
                    "description" : "직무 ID"
                  }
                }
              },
              "id" : {
                "type" : "string",
                "description" : "사용자 ID"
              },
              "email" : {
                "type" : "string",
                "description" : "이메일"
              }
            }
          }
        }
      },
      "goals-id529992684" : {
        "type" : "object",
        "properties" : {
          "duration" : {
            "type" : "object",
            "properties" : {
              "endDate" : {
                "type" : "string",
                "description" : "수정할 종료 날짜 (yyyy-MM-dd)"
              },
              "startDate" : {
                "type" : "string",
                "description" : "수정할 시작 날짜 (yyyy-MM-dd)"
              }
            },
            "description" : "수정할 목표 기간 객체"
          },
          "name" : {
            "type" : "string",
            "description" : "수정할 목표 이름"
          }
        }
      },
      "goal-retrospects-id707367061" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "todoCompletedRate" : {
                "type" : "number",
                "description" : "할일 완료율"
              },
              "goalId" : {
                "type" : "string",
                "description" : "목표 ID"
              },
              "id" : {
                "type" : "string",
                "description" : "목표회고 ID"
              },
              "analysis" : {
                "type" : "object",
                "properties" : {
                  "summary" : {
                    "type" : "string",
                    "description" : "요약"
                  },
                  "advice" : {
                    "type" : "string",
                    "description" : "조언"
                  }
                },
                "description" : "분석 결과"
              },
              "content" : {
                "type" : "string",
                "description" : "회고 내용"
              }
            },
            "description" : "응답 데이터"
          }
        }
      },
      "goals-63574136" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "planet" : {
                "type" : "object",
                "properties" : {
                  "image" : {
                    "type" : "object",
                    "properties" : {
                      "progress" : {
                        "type" : "string",
                        "description" : "진행 이미지 URL"
                      },
                      "done" : {
                        "type" : "string",
                        "description" : "완료 이미지 URL"
                      }
                    },
                    "description" : "행성 이미지 정보"
                  },
                  "name" : {
                    "type" : "string",
                    "description" : "행성 이름"
                  }
                },
                "description" : "할당된 행성 정보"
              },
              "id" : {
                "type" : "string",
                "description" : "생성된 목표 ID"
              }
            },
            "description" : "생성된 목표 정보"
          }
        }
      },
      "goal-retrospects1943232045" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "array",
            "description" : "목록 데이터 배열",
            "items" : {
              "type" : "object",
              "properties" : {
                "goalRetrospect" : {
                  "type" : "object",
                  "properties" : {
                    "id" : {
                      "type" : "string",
                      "description" : "목표 회고 ID"
                    },
                    "isCompleted" : {
                      "type" : "boolean",
                      "description" : "회고 작성 완료 여부 (내용 미작성 포함)"
                    }
                  },
                  "description" : "목표 회고 정보 (null 가능)"
                },
                "goal" : {
                  "type" : "object",
                  "properties" : {
                    "duration" : {
                      "type" : "object",
                      "properties" : {
                        "endDate" : {
                          "type" : "string",
                          "description" : "종료일 (yyyy-MM-dd)"
                        },
                        "startDate" : {
                          "type" : "string",
                          "description" : "시작일 (yyyy-MM-dd)"
                        }
                      },
                      "description" : "목표 기간"
                    },
                    "name" : {
                      "type" : "string",
                      "description" : "목표명"
                    },
                    "id" : {
                      "type" : "string",
                      "description" : "목표 ID"
                    }
                  },
                  "description" : "목표 정보"
                }
              }
            }
          }
        }
      },
      "goals-1576692806" : {
        "type" : "object",
        "properties" : {
          "duration" : {
            "type" : "object",
            "properties" : {
              "endDate" : {
                "type" : "string",
                "description" : "종료 날짜 (yyyy-MM-dd)"
              },
              "startDate" : {
                "type" : "string",
                "description" : "시작 날짜 (yyyy-MM-dd)"
              }
            },
            "description" : "목표 기간 객체"
          },
          "name" : {
            "type" : "string",
            "description" : "목표 이름"
          }
        }
      },
      "users-myprofile-promotion-954185825" : {
        "type" : "object",
        "properties" : {
          "code" : {
            "type" : "string",
            "description" : "프로모션 코드"
          }
        }
      }
    },
    "securitySchemes" : {
      "bearerAuth" : {
        "type" : "http",
        "scheme" : "bearer",
        "bearerFormat" : "JWT",
        "name" : "Authorization",
        "in" : "header",
        "description" : "Use 'your-access-token' as the value of the Authorization header"
      }
    }
  },
  "security" : [ {
    "bearerAuth" : [ ]
  } ]
}