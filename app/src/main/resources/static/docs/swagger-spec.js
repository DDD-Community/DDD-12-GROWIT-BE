window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "GrowIT API Specification",
    "description" : "GrowIT description",
    "version" : "0.0.2"
  },
  "servers" : [ {
    "url" : "http://growit-alb-alb-549641300.ap-northeast-2.elb.amazonaws.com/"
  } ],
  "tags" : [ ],
  "paths" : {
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
                  "$ref" : "#/components/schemas/auth-reissue-424105652"
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
                  "$ref" : "#/components/schemas/auth-reissue-424105652"
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
    "/goals" : {
      "get" : {
        "tags" : [ "Goals" ],
        "summary" : "내 목표 조회",
        "description" : "내 목표 조회",
        "operationId" : "get-my-goal",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals595146955"
                },
                "examples" : {
                  "get-my-goal" : {
                    "value" : "{\n  \"data\" : [ {\n    \"id\" : \"goal-1\",\n    \"name\" : \"테스트 목표\",\n    \"duration\" : {\n      \"startDate\" : \"2025-06-30\",\n      \"endDate\" : \"2025-07-06\"\n    },\n    \"beforeAfter\" : {\n      \"asIs\" : \"ASIS\",\n      \"toBe\" : \"TOBE\"\n    },\n    \"plans\" : [ {\n      \"id\" : \"planId\",\n      \"weekOfMonth\" : 1,\n      \"content\" : \"그로잇 완성\"\n    } ]\n  } ]\n}"
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
        "description" : "목표 생성",
        "operationId" : "create-goal",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goals-595510971"
              },
              "examples" : {
                "create-goal" : {
                  "value" : "{\n  \"name\" : \"내 목표는 그로잇 완성\",\n  \"duration\" : {\n    \"startDate\" : \"2025-06-23\",\n    \"endDate\" : \"2025-07-20\"\n  },\n  \"beforeAfter\" : {\n    \"asIs\" : \"기획 정의\",\n    \"toBe\" : \"배포 완료\"\n  },\n  \"plans\" : [ {\n    \"weekOfMonth\" : 1,\n    \"content\" : \"기획 및 설계 회의\"\n  }, {\n    \"weekOfMonth\" : 2,\n    \"content\" : \"디자인 시안 뽑기\"\n  }, {\n    \"weekOfMonth\" : 3,\n    \"content\" : \"프론트 개발 및 백 개발 완료\"\n  }, {\n    \"weekOfMonth\" : 4,\n    \"content\" : \"배포 완료\"\n  } ]\n}"
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
                  "$ref" : "#/components/schemas/goals574842772"
                },
                "examples" : {
                  "create-goal" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"UO-JekHK9NLdxFkE-47Q8\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/goals/{id}" : {
      "delete" : {
        "tags" : [ "Goals" ],
        "summary" : "목표 삭제",
        "description" : "목표 삭제",
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
                  "$ref" : "#/components/schemas/goals-id-1610094206"
                },
                "examples" : {
                  "delete-goal" : {
                    "value" : "{\n  \"data\" : \"삭제가 완료 되었습니다.\"\n}"
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
      }
    }
  },
  "components" : {
    "schemas" : {
      "goals595146955" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "array",
            "items" : {
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
                  "description" : "기간 정보 객체"
                },
                "plans" : {
                  "type" : "array",
                  "description" : "계획 리스트",
                  "items" : {
                    "type" : "object",
                    "properties" : {
                      "weekOfMonth" : {
                        "type" : "number",
                        "description" : "주차"
                      },
                      "id" : {
                        "type" : "string",
                        "description" : "계획 ID"
                      },
                      "content" : {
                        "type" : "string",
                        "description" : "계획 내용"
                      }
                    }
                  }
                },
                "name" : {
                  "type" : "string",
                  "description" : "목표 이름"
                },
                "id" : {
                  "type" : "string",
                  "description" : "목표 ID"
                },
                "beforeAfter" : {
                  "type" : "object",
                  "properties" : {
                    "asIs" : {
                      "type" : "string",
                      "description" : "현재 상태(As-Is)"
                    },
                    "toBe" : {
                      "type" : "string",
                      "description" : "목표 달성 후 상태(To-Be)"
                    }
                  },
                  "description" : "전/후 상태 정보 객체"
                }
              }
            }
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
      "goals-595510971" : {
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
            }
          },
          "plans" : {
            "type" : "array",
            "items" : {
              "type" : "object",
              "properties" : {
                "weekOfMonth" : {
                  "type" : "number",
                  "description" : "계획 주차"
                },
                "content" : {
                  "type" : "string",
                  "description" : "계획 내용"
                }
              }
            }
          },
          "name" : {
            "type" : "string",
            "description" : "목표 이름"
          },
          "beforeAfter" : {
            "type" : "object",
            "properties" : {
              "asIs" : {
                "type" : "string",
                "description" : "현재 상태(As-Is)"
              },
              "toBe" : {
                "type" : "string",
                "description" : "목표 달성 후 상태(To-Be)"
              }
            }
          }
        }
      },
      "auth-reissue-424105652" : {
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
      "goals-id-1610094206" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "삭제가 완료 되었습니다."
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
      "goals574842772" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "id" : {
                "type" : "string",
                "description" : "목표 ID"
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