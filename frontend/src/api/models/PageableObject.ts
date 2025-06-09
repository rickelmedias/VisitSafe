import type { SortObject } from './SortObject.ts'

export type PageableObject = {
  /**
   * @type boolean | undefined
   */
  paged?: boolean
  /**
   * @type integer | undefined, int32
   */
  pageNumber?: number
  /**
   * @type integer | undefined, int32
   */
  pageSize?: number
  /**
   * @type boolean | undefined
   */
  unpaged?: boolean
  /**
   * @type integer | undefined, int64
   */
  offset?: number
  /**
   * @type array | undefined
   */
  sort?: SortObject[]
}