import * as React from 'react'
import { FormProvider, Controller, type ControllerProps, type FieldPath, type FieldValues } from 'react-hook-form'
import { cn } from '@/lib/utils'

export function Form({ children, ...props }: React.ComponentProps<typeof FormProvider>) {
  return <FormProvider {...props}>{children}</FormProvider>
}

export function FormField<TFieldValues extends FieldValues, TName extends FieldPath<TFieldValues>>(
  props: ControllerProps<TFieldValues, TName>,
) {
  return <Controller {...props} />
}

export function FormItem({ className, ...props }: React.HTMLAttributes<HTMLDivElement>) {
  return <div className={className} {...props} />
}

export function FormLabel(props: React.LabelHTMLAttributes<HTMLLabelElement>) {
  return <label {...props} />
}

export function FormControl({ children }: { children: React.ReactNode }) {
  return <div>{children}</div>
}

export function FormMessage({ children, className }: { children?: React.ReactNode; className?: string }) {
  return <p className={cn('text-sm text-red-400', className)}>{children}</p>
}

export function FormDescription({ children, className }: { children?: React.ReactNode; className?: string }) {
  return <p className={cn('text-xs text-white/70', className)}>{children}</p>
}
