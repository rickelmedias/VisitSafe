export type JustifyServiceProviderReleasePathParams = {
  /**
   * @type string, uuid
   */
  id: string
}

/**
 * @description OK
 */
export type JustifyServiceProviderRelease200 = any

export type JustifyServiceProviderReleaseMutationRequest = string

export type JustifyServiceProviderReleaseMutationResponse = JustifyServiceProviderRelease200

export type JustifyServiceProviderReleaseMutation = {
  Response: JustifyServiceProviderRelease200
  Request: JustifyServiceProviderReleaseMutationRequest
  PathParams: JustifyServiceProviderReleasePathParams
  Errors: any
}