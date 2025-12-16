window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "GrowIT API Specification",
    "description" : "GrowIT description",
    "version" : "0.0.3"
  },
  "servers" : [ {
    "url" : "http://localhost:8081/"
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
                    "value" : "{\n  \"data\" : {\n    \"saying\" : \"오늘도 화이팅!\",\n    \"message\" : \"역시 넌 나를 실망시키지 않아\",\n    \"mood\" : \"HAPPY\"\n  }\n}"
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
                    "value" : "{\n  \"data\" : [ {\n    \"goal\" : {\n      \"id\" : \"goal-1\",\n      \"name\" : \"테스트 목표\",\n      \"duration\" : {\n        \"startDate\" : \"2025-12-15\",\n        \"endDate\" : \"2025-12-21\"\n      }\n    },\n    \"goalRetrospect\" : {\n      \"id\" : \"2vKoli0a0ArrFVWvYbeM5\",\n      \"isCompleted\" : true\n    }\n  } ]\n}"
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
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"esc8VWOaO7fKyDE_CEuSz\",\n    \"goalId\" : \"goalId\",\n    \"todoCompletedRate\" : 25,\n    \"analysis\" : {\n      \"summary\" : \"GROWIT MVP 개발과 서비스 기획을 병행하며 4주 목표를 달성\",\n      \"advice\" : \"모든 활동이 한 가지 핵심 가치에 연결되도록 중심축을 명확히 해보라냥!\"\n    },\n    \"content\" : \"이번 달 나는 '나만의 의미 있는 일'을 찾기 위해 다양한 프로젝트와 리서치에 몰입했다...\"\n  }\n}"
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
        "tags" : [ "ToDos" ],
        "summary" : "주간 ToDo 조회",
        "description" : "목표의 주간 ToDo 목록을 조회합니다.",
        "operationId" : "get-weekly-todos",
        "parameters" : [ {
          "name" : "goalId",
          "in" : "query",
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
                  "$ref" : "#/components/schemas/todos887528801"
                },
                "examples" : {
                  "get-weekly-todos" : {
                    "value" : "{\n  \"data\" : {\n    \"MONDAY\" : [ ],\n    \"TUESDAY\" : [ ],\n    \"WEDNESDAY\" : [ ],\n    \"THURSDAY\" : [ ],\n    \"FRIDAY\" : [ ],\n    \"SATURDAY\" : [ ],\n    \"SUNDAY\" : [ ]\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/todos/face/status" : {
      "get" : {
        "tags" : [ "ToDos" ],
        "summary" : "얼굴 상태 조회",
        "description" : "목표의 현재 얼굴 상태를 조회합니다.",
        "operationId" : "get-face-status",
        "parameters" : [ {
          "name" : "goalId",
          "in" : "query",
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
                  "$ref" : "#/components/schemas/todos-face-status1436938115"
                },
                "examples" : {
                  "get-face-status" : {
                    "value" : "{\n  \"data\" : \"HAPPY\"\n}"
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
      "todos887528801" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "WEDNESDAY" : {
                "type" : "array",
                "description" : "수요일 ToDo 목록",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "MONDAY" : {
                "type" : "array",
                "description" : "월요일 ToDo 목록",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "THURSDAY" : {
                "type" : "array",
                "description" : "목요일 ToDo 목록",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "SUNDAY" : {
                "type" : "array",
                "description" : "일요일 ToDo 목록",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "FRIDAY" : {
                "type" : "array",
                "description" : "금요일 ToDo 목록",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "TUESDAY" : {
                "type" : "array",
                "description" : "화요일 ToDo 목록",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "SATURDAY" : {
                "type" : "array",
                "description" : "토요일 ToDo 목록",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              }
            },
            "description" : "요일별 ToDo 목록"
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
      "goal-retrospects-id130578538" : {
        "type" : "object",
        "properties" : {
          "content" : {
            "type" : "string",
            "description" : "회고 내용"
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
      "todos-face-status1436938115" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "얼굴 상태 (HAPPY, NORMAL, SAD)"
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