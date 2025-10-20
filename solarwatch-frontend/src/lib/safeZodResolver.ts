import type { Resolver } from 'react-hook-form'
import { z } from 'zod'

export function safeZodResolver<TSchema extends z.ZodTypeAny>(schema: TSchema): Resolver<any, any> {
  return async (values: any) => {
    const result = schema.safeParse(values)
    if (result.success) {
      return { values: result.data, errors: {} } as any
    }
    const errors: Record<string, any> = {}
    for (const issue of result.error.issues) {
      const key = Array.isArray(issue.path) && issue.path.length > 0 ? issue.path[0] : undefined
      if (typeof key === 'string') {
        errors[key] = { type: 'zod', message: issue.message || 'Invalid value' }
      }
    }
    return { values: {}, errors } as any
  }
}
